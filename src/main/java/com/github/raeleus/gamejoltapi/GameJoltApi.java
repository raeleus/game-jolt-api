package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.StreamUtils;
import com.github.raeleus.gamejoltapi.GameJoltScores.*;
import com.github.raeleus.gamejoltapi.GameJoltUsers.GameJoltUser;
import lombok.NonNull;
import lombok.var;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
 * @see GameJoltApi#sendBatchRequest(Array, String, String, Boolean, Boolean, GameJoltListener)
 * @see GameJoltApi#addGuestScore(String, String, String, long)
 * @see GameJoltApi#downloadScores(String, String, ScoreListener)
 */
public class GameJoltApi {
    private final static String apiURL = "https://api.gamejolt.com/api/game/";
    private final static String version = "v1_2";
    private MessageDigest crypt;
    private JsonReader jsonReader;

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
     * provides a few ways to automatically pass your game this information. HTML5 Games hosted on gamejolt.com are
     * provided the username and password through URL parameters. Executable games launched from the Game Jolt app pass
     * this information through an automatically generated file called .gj-credentials placed next to the game's
     * executable.
     * <p>
     * This method uses these credentials on their associated backends to attempt to authenticate the user. In the case
     * that this fails, you should provide a means to manually log the user in.
     */
    public void autoAuthenticateUser() {

    }

    /**
     * A Game Jolt user often has an avatar associated with their account. This can be accessed by your game to
     * represent their player in your highscores list, for example. This is provided by {@link
     * GameJoltUser#getAvatarURL()} as a String. This method downloads the image from
     * the submitted URL and returns the avatar as a texture.
     */
    public void downloadImageUrlAsTexture(String url, GameJoltTextureListener listener) {
        byte[] bytes = new byte[200 * 1024];
        int numBytes = downloadUrl(bytes, url);
        if (numBytes != 0) {
            Pixmap pixmap = new Pixmap(bytes, 0, numBytes);
            int width = MathUtils.nextPowerOfTwo(pixmap.getWidth());
            int height = MathUtils.nextPowerOfTwo(pixmap.getHeight());
            final Pixmap potPixmap = new Pixmap(width, height, pixmap.getFormat());
            potPixmap.setBlending(Blending.None);
            potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
            pixmap.dispose();
            Gdx.app.postRunnable(() -> listener.downloaded(new Texture(potPixmap)));
        }
    }

    public interface GameJoltTextureListener {
        void downloaded(Texture texture);
    }

    private int downloadUrl(byte[] out, String url) {
        InputStream in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setUseCaches(true);
            conn.connect();
            in = conn.getInputStream();
            int readBytes = 0;
            while (true) {
                int length = in.read(out, readBytes, out.length - readBytes);
                if (length == -1) break;
                readBytes += length;
            }
            return readBytes;
        } catch (Exception ex) {
            return 0;
        } finally {
            StreamUtils.closeQuietly(in);
        }
    }

    /**
     * Convenience method to add a guest score without having to use the full API.
     * @param gameID The ID of your game. Required.
     * @param key The game's private key used to encrypt the request signature. Required.
     * @param guest The guest user's name. Required.
     * @param score This is a numerical sorting value associated with the score. All sorting will be based on this
     *              number. It will also be parsed as a String for the display score. Required.
     */
    public void addGuestScore(@NonNull String gameID, @NonNull String key, @NonNull String guest, long score) {
        addGuestScore(gameID, key, guest, score, null, null);
    }

    /**
     * Convenience method to add a guest score without having to use the full API.
     * @param gameID The ID of your game. Required.
     * @param key The game's private key used to encrypt the request signature. Required.
     * @param guest The guest user's name. Required.
     * @param score This is a numerical sorting value associated with the score. All sorting will be based on this
     *              number. It will also be parsed as a String for the display score. Required.
     * @param listener The listener called when the response is received. Optional.
     */
    public void addGuestScore(@NonNull String gameID, @NonNull String key, @NonNull String guest, long score, GameJoltListener listener) {
        addGuestScore(gameID, key, guest, score, null, listener);
    }

    /**
     * Convenience method to add a guest score without having to use the full API.
     * @param gameID The ID of your game. Required.
     * @param key The game's private key used to encrypt the request signature. Required.
     * @param guest The guest user's name. Required.
     * @param score This is a numerical sorting value associated with the score. All sorting will be based on this
     *              number. It will also be parsed as a String for the display score. Required.
     * @param tableID The ID of the score table to submit to. Optional.
     * @param listener The listener called when the response is received. Optional.
     */
    public void addGuestScore(@NonNull String gameID, @NonNull String key, @NonNull String guest, long score, Integer tableID, GameJoltListener listener) {
        var request = ScoresAddRequest.builder()
            .gameID(gameID)
            .guest(guest)
            .score(Long.toString(score))
            .sort(score)
            .tableID(tableID)
            .build();

        sendRequest(request, key, listener != null ? listener : new ScoresAddListener() {
            @Override
            public void scoresAdd(JsonValue jsonValue, ScoresAddData data) {

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
     * @param gameID The ID of your game. Required.
     * @param key The game's private key used to encrypt the request signature. Required.
     * @param listener
     */
    public void downloadScores(@NonNull String gameID, @NonNull String key, @NonNull ScoreListener listener) {
        downloadScores(gameID, key, null, null, listener);
    }

    /**
     * Convenience method to retrieve the scores from a score table without having to use the full API.
     * @param gameID The ID of your game. Required.
     * @param key The game's private key used to encrypt the request signature. Required.
     * @param limit The number of scores you'd like to return. The default value is 10 scores. The maximum amount of
     *              scores you can retrieve is 100. Optional.
     * @param listener
     */
    public void downloadScores(@NonNull String gameID, @NonNull String key, Integer limit, @NonNull ScoreListener listener) {
        downloadScores(gameID, key, limit, null, listener);
    }

    /**
     * Convenience method to retrieve the scores from a score table without having to use the full API.
     * @param gameID The ID of your game. Required.
     * @param key The game's private key used to encrypt the request signature. Required.
     * @param limit The number of scores you'd like to return. The default value is 10 scores. The maximum amount of
     *              scores you can retrieve is 100. Optional.
     * @param tableID The ID of the score table. Optional.
     * @param listener
     */
    public void downloadScores(@NonNull String gameID, @NonNull String key, Integer limit, Integer tableID, @NonNull ScoreListener listener) {
        var request = ScoresFetchRequest.builder()
            .gameID(gameID)
            .limit(limit)
            .tableID(tableID)
            .build();

        sendRequest(request, key, new GameJoltListener() {
            @Override
            public void response(JsonValue jsonValue, GameJoltData data) {
                var fetchResult = (ScoresFetchData) data;
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

    public interface ScoreListener {
        void downloaded(Array<GameJoltScore> scores);
    }

    /**
     * Sends a single request to the Game Jolt server. When the response is received, the corresponding listener methods
     * are called depending on the result. See <a href="https://gamejolt.com/game-api/doc/construction">Game Jolt API
     * Request Construction</a> for more details.
     *
     * @param request The {@link GameJoltRequest} describing this API request.
     * @param key The game's private key used to encrypt the request signature.
     * @param listener The listener called when the response is received.
     */
    public void sendRequest(@NonNull GameJoltRequest request, @NonNull String key, @NonNull GameJoltListener listener) {
        var url = apiURL + version + request;
        Net.HttpRequest httpRequest = new Net.HttpRequest(HttpMethods.GET);
        String signature = encrypt(url + key);
        httpRequest.setUrl(url + "&signature=" + signature);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                var response = httpResponse.getResultAsString();
                var jsonValue = jsonReader.parse(response).child();
                listener.response(jsonValue, request.handleResponse(jsonValue));
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
     * @param requests A list of {@link GameJoltRequest} that will be sent in batch to the Game Jolt server.
     * @param gameID The ID of your game.
     * @param key The game's private key used to encrypt the request signature.
     * @param parallel By default, each sub-request is processed on the servers sequentially. If this is set to true,
     *                 then all sub-requests are processed at the same time, without waiting for the previous
     *                 sub-request to finish before the next one is started.
     * @param breakOnError If this is set to true, one sub-request failure will cause the entire batch to stop
     *                     processing subsequent sub-requests and return a value of false for success.
     * @param listener The listener called when the response is received. The listener will be called on each request
     *                 submitted.
     */
    public void sendBatchRequest(@NonNull Array<GameJoltRequest> requests, @NonNull String gameID, @NonNull String key, Boolean parallel, Boolean breakOnError, @NonNull GameJoltListener listener) {
        StringBuilder url = new StringBuilder(apiURL).append(version).append("/batch/?game_id=").append(gameID);
        if (parallel != null && parallel) url.append("&parallel=").append(parallel);
        else if (breakOnError != null && breakOnError) url.append("&break_on_error=").append(breakOnError);
        for (var request : requests) {
            var subUrl = request.toString();
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
                var response = httpResponse.getResultAsString();
                var jsonValue = jsonReader.parse(response).child();
                if (jsonValue.getBoolean("success", false)) {
                    jsonValue = jsonValue.get("responses");
                    if (requests.size != jsonValue.size) {
                        listener.cancelled();
                        return;
                    }
                    int requestIndex = 0;
                    for (var childValue : jsonValue.iterator()) {
                        var request = requests.get(requestIndex++);
                        listener.response(childValue, request.handleResponse(childValue));
                    }
                }
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
     * Creates an MD5 hash of the provided message.
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
     * @param string The String to encode.
     * @return The encoded String.
     */
    static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, "utf-8").replace("+", "%20");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
