package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.DownloadPixmapResponseListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.raeleus.gamejoltapi.GameJoltScores.*;
import com.github.raeleus.gamejoltapi.GameJoltUsers.GameJoltUser;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <a href="https://gamejolt.com/learn">Game Jolt</a> is a platform for uploading and sharing games. The GameJoltAPI
 * provides methods to send and receive data from the Game Jolt server. This allows you to record high scores, save
 * player data to the cloud, award trophies to users, and more. This is achieved by sending {@link GameJoltRequest}s.
 * These can be sent individually or in batch to improve speed. There are utility methods to make basic use of the API
 * accessible to beginner users.
 *
 * @see GameJoltApi#sendRequest(GameJoltRequest, String, GameJoltListener)
 * @see GameJoltApi#sendBatchRequest(Array, String, String, Boolean, Boolean, GameJoltListener...)
 * @see GameJoltApi#addGuestScore(String, String, String, long)
 * @see GameJoltApi#downloadScores(String, String, ScoreListener)
 */
public class GameJoltApi {
    private final static String apiURL = "https://api.gamejolt.com/api/game/";
    private final static String version = "v1_2";
    private final MessageDigest crypt;
    private final JsonReader jsonReader;
    
    public GameJoltApi() {
        try {
            crypt = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        
        jsonReader = new JsonReader();
    }
    
    /**
     * Prompting the user to enter their username and token is usually disruptive to the flow of your game. Game Jolt
     * provides a couple ways to automatically pass your game this information. Executable games launched from the Game
     * Jolt app pass this information through an automatically generated file called .gj-credentials placed next to the
     * game's executable.
     * <p>
     * This method returns these credentials as username/token pair. In the case that this fails, you should provide a
     * means to manually log the user in. This method will not work if the user has downloaded the game from the website
     * directly and does not use the Game Jolt app.
     * <p>
     * To log in from HTML5, see the <a href="https://github.com/raeleus/game-jolt-api/wiki#html">wiki</a>
     *
     * @return The username and token pair. Will return null if no valid file was found.
     */
    public GameJoltUsernameTokenPair autoRetrieveUsernameAndToken() {
        if (Gdx.app.getType() != ApplicationType.Desktop) return null;
        FileHandle file = Gdx.files.local(".gj-credentials");
        if (!file.exists()) return null;
        
        String[] lines = file.readString().split("\\n");
        if (lines.length < 3) return null;
        return new GameJoltUsernameTokenPair(lines[1], lines[2]);
    }
    
    public static class GameJoltUsernameTokenPair {
        public String username;
        public String token;
        
        public GameJoltUsernameTokenPair(String username, String token) {
            this.username = username;
            this.token = token;
        }
    }
    
    /**
     * A Game Jolt user often has an avatar associated with their account. This can be accessed by your game to
     * represent their player in your highscores list, for example. This is provided by
     * {@link GameJoltUser#getAvatarURL()} as a String. This method downloads the image from the submitted URL and
     * returns the avatar as a texture. It will modify the URL returned to PNG if the submitted URL is WEBP.
     */
    public void downloadImageUrlAsTextureRegion(String url, GameJoltTextureListener listener) {
        url = url.replaceAll("(?i)\\.webp$", ".png");
        Pixmap.downloadFromUrl(url, new DownloadPixmapResponseListener() {
            @Override
            public void downloadComplete(Pixmap pixmap) {
                int width = MathUtils.nextPowerOfTwo(pixmap.getWidth());
                int height = MathUtils.nextPowerOfTwo(pixmap.getHeight());
                final Pixmap potPixmap = new Pixmap(width, height, pixmap.getFormat());
                potPixmap.setBlending(Blending.None);
                potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
                pixmap.dispose();
                Gdx.app.postRunnable(() -> {
                    Texture texture = new Texture(potPixmap);
                    TextureRegion region = new TextureRegion(texture, pixmap.getWidth(), pixmap.getHeight());
                    listener.downloaded(region);
                });
            }
            
            @Override
            public void downloadFailed(Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
    
    /**
     * The listener for the {@link GameJoltApi#downloadImageUrlAsTextureRegion(String, GameJoltTextureListener)}.
     */
    public interface GameJoltTextureListener {
        void downloaded(TextureRegion region);
    }
    
    /**
     * Convenience method to add a guest score without having to use the full API.
     *
     * @param gameID The ID of your game. Required.
     * @param key    The game's private key used to encrypt the request signature. Required.
     * @param guest  The guest user's name. Required.
     * @param score  This is a numerical sorting value associated with the score. All sorting will be based on this
     *               number. It will also be parsed as a String for the display score. Required.
     */
    public void addGuestScore( String gameID,  String key,  String guest, long score) {
        addGuestScore(gameID, key, guest, score, null, null);
    }
    
    /**
     * Convenience method to add a guest score without having to use the full API.
     *
     * @param gameID   The ID of your game. Required.
     * @param key      The game's private key used to encrypt the request signature. Required.
     * @param guest    The guest user's name. Required.
     * @param score    This is a numerical sorting value associated with the score. All sorting will be based on this
     *                 number. It will also be parsed as a String for the display score. Required.
     * @param listener The listener called when the response is received. Optional.
     */
    public void addGuestScore( String gameID,  String key,  String guest, long score,
                              GameJoltListener listener) {
        addGuestScore(gameID, key, guest, Long.toString(score), score, null, listener);
    }
    
    /**
     * Convenience method to add a guest score without having to use the full API.
     *
     * @param gameID   The ID of your game. Required.
     * @param key      The game's private key used to encrypt the request signature. Required.
     * @param guest    The guest user's name. Required.
     * @param score    This is a numerical sorting value associated with the score. All sorting will be based on this
     *                 number. It will also be parsed as a String for the display score. Required.
     * @param tableID  The ID of the score table to submit to. Optional.
     * @param listener The listener called when the response is received. Optional.
     */
    public void addGuestScore( String gameID,  String key,  String guest, long score,
                              Integer tableID, GameJoltListener listener) {
        addGuestScore(gameID, key, guest, Long.toString(score), score, tableID, listener);
    }
    
    /**
     * Convenience method to add a guest score without having to use the full API.
     *
     * @param gameID   The ID of your game. Required.
     * @param key      The game's private key used to encrypt the request signature. Required.
     * @param guest    The guest user's name. Required.
     * @param score    The display score shown on Game Jolt high-score table. Required.
     * @param sort     This is a numerical sorting value associated with the score. All sorting will be based on this
     *                 number. Required.
     * @param tableID  The ID of the score table to submit to. Optional.
     * @param listener The listener called when the response is received. Optional.
     */
    public void addGuestScore( String gameID,  String key,  String guest,  String score,
                              long sort, Integer tableID, GameJoltListener listener) {
        ScoresAddRequest request = ScoresAddRequest.builder()
                .gameID(gameID)
                .guest(guest)
                .score(score)
                .sort(sort)
                .tableID(tableID)
                .build();
        
        sendRequest(request, key, listener != null ? listener : new ScoresAddListener() {
            @Override
            public void scoresAdd(ScoresAddValue value) {
            
            }
            
            @Override
            public void failed(Throwable t) {
                throw new RuntimeException(t);
            }
            
            @Override
            public void cancelled() {
                throw new RuntimeException("Connection cancelled while adding guest score.");
            }
        });
    }
    
    /**
     * Convenience method to retrieve the scores from a score table without having to use the full API.
     *
     * @param gameID   The ID of your game. Required.
     * @param key      The game's private key used to encrypt the request signature. Required.
     * @param listener
     */
    public void downloadScores( String gameID,  String key,  ScoreListener listener) {
        downloadScores(gameID, key, null, null, listener);
    }
    
    /**
     * Convenience method to retrieve the scores from a score table without having to use the full API.
     *
     * @param gameID   The ID of your game. Required.
     * @param key      The game's private key used to encrypt the request signature. Required.
     * @param limit    The number of scores you'd like to return. The default value is 10 scores. The maximum amount of
     *                 scores you can retrieve is 100. Optional.
     * @param listener
     */
    public void downloadScores( String gameID,  String key, Integer limit,
                                ScoreListener listener) {
        downloadScores(gameID, key, limit, null, listener);
    }
    
    /**
     * Convenience method to retrieve the scores from a score table without having to use the full API.
     *
     * @param gameID   The ID of your game. Required.
     * @param key      The game's private key used to encrypt the request signature. Required.
     * @param limit    The number of scores you'd like to return. The default value is 10 scores. The maximum amount of
     *                 scores you can retrieve is 100. Optional.
     * @param tableID  The ID of the score table. Optional.
     * @param listener
     */
    public void downloadScores( String gameID,  String key, Integer limit, Integer tableID,
                                ScoreListener listener) {
        ScoresFetchRequest request = ScoresFetchRequest.builder()
                .gameID(gameID)
                .limit(limit)
                .tableID(tableID)
                .build();
        
        sendRequest(request, key, new GameJoltListener() {
            @Override
            public void response(GameJoltRequest request, GameJoltValue value) {
                ScoresFetchValue fetchResult = (ScoresFetchValue) value;
                listener.downloaded(fetchResult.scores);
            }
            
            @Override
            public void failed(Throwable t) {
                throw new RuntimeException(t);
            }
            
            @Override
            public void cancelled() {
                throw new RuntimeException("Connection cancelled while downloading scores.");
            }
        });
    }
    
    /**
     * The listener for the {@link GameJoltApi#downloadScores(String, String, Integer, Integer, ScoreListener)}.
     */
    public interface ScoreListener {
        void downloaded(Array<GameJoltScore> scores);
    }
    
    /**
     * Sends a single request to the Game Jolt server. When the response is received, the corresponding listener methods
     * are called depending on the result. See <a href="https://gamejolt.com/game-api/doc/construction">Game Jolt API
     * Request Construction</a> for more details.
     *
     * @param request  The {@link GameJoltRequest} describing this API request.
     * @param key      The game's private key used to encrypt the request signature.
     * @param listener The listener called when the response is received.
     */
    public void sendRequest( GameJoltRequest request,  String key,  GameJoltListener listener) {
        String url = apiURL + version + request;
        Net.HttpRequest httpRequest = new Net.HttpRequest(HttpMethods.GET);
        String signature = encrypt(url + key);
        httpRequest.setUrl(url + "&signature=" + signature);
        
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                JsonValue jsonValue = jsonReader.parse(response).child();
                Gdx.app.postRunnable(() -> {
                    try {
                        listener.response(request, request.handleResponse(jsonValue));
                    } catch (Exception e) {
                        failed(new Exception(response, e));
                    }
                });
            }
            
            @Override
            public void failed(Throwable t) {
                listener.failed(t);
            }
            
            @Override
            public void cancelled() {
                listener.cancelled();
            }
        });
    }
    
    /**
     * Sends a batch of sub-requests to the Game Jolt server. This enables you to send multiple API calls with one HTTP
     * request. When the response is received, the corresponding listener methods are called depending on the result.
     * See <a href="https://gamejolt.com/game-api/doc/construction">Game Jolt API Request Construction</a> for more
     * details.
     * <p>
     * The maximum amount of sub requests in one batch request is 50. The parallel and breakOnError parameters cannot be
     * used in the same request.
     *
     * @param requests     A list of {@link GameJoltRequest} that will be sent in batch to the Game Jolt server.
     * @param gameID       The ID of your game.
     * @param key          The game's private key used to encrypt the request signature.
     * @param parallel     By default, each sub-request is processed on the servers sequentially. If this is set to
     *                     true, then all sub-requests are processed at the same time, without waiting for the previous
     *                     sub-request to finish before the next one is started.
     * @param breakOnError If this is set to true, one sub-request failure will cause the entire batch to stop
     *                     processing subsequent sub-requests and return a value of false for success.
     * @param listeners    The listeners called when the response is received. Each listener will be called on each
     *                     request submitted. If any request fails or is cancelled, all listeners will be notified.
     */
    public void sendBatchRequest( Array<GameJoltRequest> requests,  String gameID,  String key,
                                 Boolean parallel, Boolean breakOnError,  GameJoltListener... listeners) {
        StringBuilder url = new StringBuilder(apiURL).append(version).append("/batch/?game_id=").append(gameID);
        if (parallel != null && parallel) url.append("&parallel=").append(parallel);
        else if (breakOnError != null && breakOnError) url.append("&break_on_error=").append(breakOnError);
        for (GameJoltRequest request : requests) {
            String subUrl = request.toString();
            String signature = encrypt(subUrl + key);
            subUrl += "&signature=" + signature;
            subUrl = "&requests[]=" + urlEncode(subUrl);
            url.append(subUrl);
        }
        
        Net.HttpRequest httpRequest = new Net.HttpRequest(HttpMethods.GET);
        String signature = encrypt(url + key);
        httpRequest.setUrl(url + "&signature=" + signature);
        
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                JsonValue jsonValue = jsonReader.parse(response).child();
                if (jsonValue.getBoolean("success", false)) {
                    jsonValue = jsonValue.get("responses");
                    if (requests.size != jsonValue.size) {
                        cancelled();
                        return;
                    }
                    int requestIndex = 0;
                    for (JsonValue childValue : jsonValue.iterator()) {
                        GameJoltRequest request = requests.get(requestIndex++);
                        for (GameJoltListener listener : listeners) {
                            listener.response(request, request.handleResponse(childValue));
                        }
                    }
                } else {
                    JsonValue responses = jsonValue.get("responses");
                    Throwable t = new Throwable("Batch request failed:\n" + jsonValue.getString("message",
                            "No error returned from server.") + " \"" + responses.get(responses.size - 1)
                            .getString("message", "") + "\"");
                    for (GameJoltListener listener : listeners) {
                        listener.failed(t);
                    }
                }
            }
            
            @Override
            public void failed(Throwable t) {
                for (GameJoltListener listener : listeners) {
                    listener.failed(t);
                }
            }
            
            @Override
            public void cancelled() {
                for (GameJoltListener listener : listeners) {
                    listener.cancelled();
                }
            }
        });
    }
    
    /**
     * Creates an MD5 hash of the provided message.
     *
     * @param message The message to encrypt.
     * @return The MD5 hash.
     */
    private String encrypt(String message) {
        crypt.reset();
        try {
            final byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            final byte[] digest = crypt.digest(bytes);
            final StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Converts the characters of the provided String into a format that can be transmitted over the internet.
     *
     * @param string The String to encode.
     * @return The encoded String.
     */
    static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, StandardCharsets.UTF_8.toString()).replace("+", "%20");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
