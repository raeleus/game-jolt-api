package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * Game Jolt allows you to add trophies to your games!
 * <p>
 * Trophies come in four materials: bronze, silver, gold, and platinum. This is to reflect how difficult it is to
 * achieve a trophy. A bronze trophy should be easy to achieve, whereas a platinum trophy should be very hard to
 * achieve.
 * <p>
 * On Game Jolt, trophies are always listed in order from easiest to most difficult to achieve.
 * <p>
 * You can also tag trophies on the site as "secret". A sercet trophy's image and description is not visible until a
 * gamer has achieved it.
 */
public class GameJoltTrophies {
    /**
     * Returns one trophy or multiple trophies, depending on the parameters passed in.
     */
    public static class TrophiesFetchRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The user's username. Required.
         */
        @NonNull
        private String username;
        
        /**
         * The user's token. Required.
         */
        @NonNull
        private String userToken;
        
        /**
         * Pass in true to return only the achieved trophies for a user. Pass in false to return only trophies the user
         * hasn't achieved. Leave null to retrieve all trophies. Optional.
         */
        private Boolean achieved;
        
        /**
         * The ID's of the trophies you'd like to fetch. Optional.
         */
        private List<Integer> trophyIDs;
        
        TrophiesFetchRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken,
                             Boolean achieved, List<Integer> trophyIDs) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
            this.achieved = achieved;
            this.trophyIDs = trophyIDs;
        }
        
        public static TrophiesFetchRequestBuilder builder() {
            return new TrophiesFetchRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/trophies/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));
            if (achieved != null) builder.append("&achieved=").append(achieved);
            if (trophyIDs != null && !trophyIDs.isEmpty()) {
                builder.append("&trophy_id=").append(trophyIDs.get(0));
                for (int i = 1; i < trophyIDs.size(); i++) {
                    var trophyID = trophyIDs.get(i);
                    builder.append(",").append(trophyID);
                }
            }
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link TrophiesFetchValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link TrophiesFetchValue} with the values returned from the server.
         */
        @Override
        public TrophiesFetchValue handleResponse(JsonValue jsonValue) {
            
            var trophies = new Array<GameJoltTrophy>();
            var trophiesJsonValue = jsonValue.get("trophies");
            for (JsonValue trophyJsonValue : trophiesJsonValue.iterator()) {
                var trophy = GameJoltTrophy.builder()
                        .id(trophyJsonValue.getInt("id"))
                        .title(trophyJsonValue.getString("title"))
                        .description(trophyJsonValue.getString("description"))
                        .difficulty(TrophyDifficulty.valueOf(
                                trophyJsonValue.getString("difficulty").toUpperCase(Locale.ROOT)))
                        .imageURL(trophyJsonValue.getString("image_url"))
                        .achieved(trophyJsonValue.getString("achieved"))
                        .build();
                trophies.add(trophy);
            }
            
            return TrophiesFetchValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getUsername() {
            return this.username;
        }
        
        public @NonNull String getUserToken() {
            return this.userToken;
        }
        
        public Boolean getAchieved() {
            return this.achieved;
        }
        
        public List<Integer> getTrophyIDs() {
            return this.trophyIDs;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setUsername(@NonNull String username) {
            this.username = username;
        }
        
        public void setUserToken(@NonNull String userToken) {
            this.userToken = userToken;
        }
        
        public void setAchieved(Boolean achieved) {
            this.achieved = achieved;
        }
        
        public void setTrophyIDs(List<Integer> trophyIDs) {
            this.trophyIDs = trophyIDs;
        }
        
        public static class TrophiesFetchRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            private Boolean achieved;
            private ArrayList<Integer> trophyIDs;
            
            TrophiesFetchRequestBuilder() {
            }
            
            public TrophiesFetchRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public TrophiesFetchRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public TrophiesFetchRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public TrophiesFetchRequestBuilder achieved(Boolean achieved) {
                this.achieved = achieved;
                return this;
            }
            
            public TrophiesFetchRequestBuilder trophyID(Integer trophyID) {
                if (this.trophyIDs == null) this.trophyIDs = new ArrayList<Integer>();
                this.trophyIDs.add(trophyID);
                return this;
            }
            
            public TrophiesFetchRequestBuilder trophyIDs(Collection<? extends Integer> trophyIDs) {
                if (trophyIDs == null) {
                    throw new NullPointerException("trophyIDs cannot be null");
                }
                if (this.trophyIDs == null) this.trophyIDs = new ArrayList<Integer>();
                this.trophyIDs.addAll(trophyIDs);
                return this;
            }
            
            public TrophiesFetchRequestBuilder clearTrophyIDs() {
                if (this.trophyIDs != null)
                    this.trophyIDs.clear();
                return this;
            }
            
            public TrophiesFetchRequest build() {
                List<Integer> trophyIDs;
                switch (this.trophyIDs == null ? 0 : this.trophyIDs.size()) {
                    case 0:
                        trophyIDs = java.util.Collections.emptyList();
                        break;
                    case 1:
                        trophyIDs = java.util.Collections.singletonList(this.trophyIDs.get(0));
                        break;
                    default:
                        trophyIDs = java.util.Collections.unmodifiableList(new ArrayList<Integer>(this.trophyIDs));
                }
                
                return new TrophiesFetchRequest(this.gameID, this.username, this.userToken, this.achieved, trophyIDs);
            }
            
            public String toString() {
                return "GameJoltTrophies.TrophiesFetchRequest.TrophiesFetchRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ", achieved=" + this.achieved + ", trophyIDs=" + this.trophyIDs + ")";
            }
        }
    }
    
    /**
     * The result of fetching the trophies.
     */
    public static class TrophiesFetchValue implements GameJoltValue {
        
        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;
        
        /**
         * The request that triggered the response.
         */
        public GameJoltRequest request;
        
        /**
         * Whether the request succeeded or failed.
         */
        public boolean success;
        
        /**
         * If the request was not successful, this contains the error message.
         */
        public String message;
        
        /**
         * The list of trophies returned from the fetch.
         */
        public Array<GameJoltTrophy> trophies;
        
        TrophiesFetchValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message,
                           Array<GameJoltTrophy> trophies) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.trophies = trophies;
        }
        
        public static TrophiesFetchValueBuilder builder() {
            return new TrophiesFetchValueBuilder();
        }
        
        public JsonValue getJsonValue() {
            return this.jsonValue;
        }
        
        public GameJoltRequest getRequest() {
            return this.request;
        }
        
        public boolean isSuccess() {
            return this.success;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public Array<GameJoltTrophy> getTrophies() {
            return this.trophies;
        }
        
        public void setJsonValue(JsonValue jsonValue) {
            this.jsonValue = jsonValue;
        }
        
        public void setRequest(GameJoltRequest request) {
            this.request = request;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public void setTrophies(Array<GameJoltTrophy> trophies) {
            this.trophies = trophies;
        }
        
        public static class TrophiesFetchValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private Array<GameJoltTrophy> trophies;
            
            TrophiesFetchValueBuilder() {
            }
            
            public TrophiesFetchValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public TrophiesFetchValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public TrophiesFetchValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public TrophiesFetchValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public TrophiesFetchValueBuilder trophies(Array<GameJoltTrophy> trophies) {
                this.trophies = trophies;
                return this;
            }
            
            public TrophiesFetchValue build() {
                return new TrophiesFetchValue(this.jsonValue, this.request, this.success, this.message, this.trophies);
            }
            
            public String toString() {
                return "GameJoltTrophies.TrophiesFetchValue.TrophiesFetchValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", trophies=" + this.trophies + ")";
            }
        }
    }
    
    /**
     * Listener for {@link TrophiesFetchRequest}. Override
     * {@link TrophiesFetchListener#trophiesFetch(TrophiesFetchValue)} to handle the server response.
     */
    public static abstract class TrophiesFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof TrophiesFetchValue) trophiesFetch((TrophiesFetchValue) value);
        }
        
        public abstract void trophiesFetch(TrophiesFetchValue value);
    }
    
    /**
     * The trophy data.
     */
    public static class GameJoltTrophy {
        
        /**
         * The ID of the trophy.
         */
        public int id;
        
        /**
         * The title of the trophy on the site.
         */
        public String title;
        
        /**
         * The trophy description text.
         */
        public String description;
        
        /**
         * Bronze, Silver, Gold, or Platinum
         */
        public TrophyDifficulty difficulty;
        
        /**
         * The URL of the trophy's thumbnail image.
         */
        public String imageURL;
        
        /**
         * Date/time when the trophy was achieved by the user. Null if it hasn't been achieved.
         */
        public String achieved;
        
        GameJoltTrophy(int id, String title, String description, TrophyDifficulty difficulty, String imageURL,
                       String achieved) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.difficulty = difficulty;
            this.imageURL = imageURL;
            this.achieved = achieved;
        }
        
        public static GameJoltTrophyBuilder builder() {
            return new GameJoltTrophyBuilder();
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        public String getDescription() {
            return this.description;
        }
        
        public TrophyDifficulty getDifficulty() {
            return this.difficulty;
        }
        
        public String getImageURL() {
            return this.imageURL;
        }
        
        public String getAchieved() {
            return this.achieved;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public void setDifficulty(TrophyDifficulty difficulty) {
            this.difficulty = difficulty;
        }
        
        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }
        
        public void setAchieved(String achieved) {
            this.achieved = achieved;
        }
        
        public static class GameJoltTrophyBuilder {
            private int id;
            private String title;
            private String description;
            private TrophyDifficulty difficulty;
            private String imageURL;
            private String achieved;
            
            GameJoltTrophyBuilder() {
            }
            
            public GameJoltTrophyBuilder id(int id) {
                this.id = id;
                return this;
            }
            
            public GameJoltTrophyBuilder title(String title) {
                this.title = title;
                return this;
            }
            
            public GameJoltTrophyBuilder description(String description) {
                this.description = description;
                return this;
            }
            
            public GameJoltTrophyBuilder difficulty(TrophyDifficulty difficulty) {
                this.difficulty = difficulty;
                return this;
            }
            
            public GameJoltTrophyBuilder imageURL(String imageURL) {
                this.imageURL = imageURL;
                return this;
            }
            
            public GameJoltTrophyBuilder achieved(String achieved) {
                this.achieved = achieved;
                return this;
            }
            
            public GameJoltTrophy build() {
                return new GameJoltTrophy(this.id, this.title, this.description, this.difficulty, this.imageURL,
                        this.achieved);
            }
            
            public String toString() {
                return "GameJoltTrophies.GameJoltTrophy.GameJoltTrophyBuilder(id=" + this.id + ", title=" + this.title + ", description=" + this.description + ", difficulty=" + this.difficulty + ", imageURL=" + this.imageURL + ", achieved=" + this.achieved + ")";
            }
        }
    }
    
    /**
     * The difficulty of a trophy.
     */
    public enum TrophyDifficulty {
        BRONZE("Bronze"), SILVER("Silver"), GOLD("Gold"), PLATINUM("Platinum");
        
        public String name;
        
        TrophyDifficulty(String name) {
            this.name = name;
        }
    }
    
    /**
     * Sets a trophy as achieved for a particular user.
     */
    public static class TrophiesAddAchievedRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The user's username. Required.
         */
        @NonNull
        private String username;
        
        /**
         * The user's token. Required.
         */
        @NonNull
        private String userToken;
        
        /**
         * The ID of the trophy to add for the user. Required.
         */
        @NonNull
        private Integer trophyID;
        
        TrophiesAddAchievedRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken,
                                   @NonNull Integer trophyID) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
            this.trophyID = trophyID;
        }
        
        public static TrophiesAddAchievedRequestBuilder builder() {
            return new TrophiesAddAchievedRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            String builder = "/trophies/add-achieved/?game_id=" + gameID +
                    "&username=" + urlEncode(username) +
                    "&user_token=" + urlEncode(userToken) +
                    "&trophy_id=" + trophyID;
            
            return builder;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link TrophiesAddAchievedValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link TrophiesAddAchievedValue} with the values returned from the server.
         */
        @Override
        public TrophiesAddAchievedValue handleResponse(JsonValue jsonValue) {
            return TrophiesAddAchievedValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getUsername() {
            return this.username;
        }
        
        public @NonNull String getUserToken() {
            return this.userToken;
        }
        
        public @NonNull Integer getTrophyID() {
            return this.trophyID;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setUsername(@NonNull String username) {
            this.username = username;
        }
        
        public void setUserToken(@NonNull String userToken) {
            this.userToken = userToken;
        }
        
        public void setTrophyID(@NonNull Integer trophyID) {
            this.trophyID = trophyID;
        }
        
        public static class TrophiesAddAchievedRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            private @NonNull Integer trophyID;
            
            TrophiesAddAchievedRequestBuilder() {
            }
            
            public TrophiesAddAchievedRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public TrophiesAddAchievedRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public TrophiesAddAchievedRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public TrophiesAddAchievedRequestBuilder trophyID(@NonNull Integer trophyID) {
                this.trophyID = trophyID;
                return this;
            }
            
            public TrophiesAddAchievedRequest build() {
                return new TrophiesAddAchievedRequest(this.gameID, this.username, this.userToken, this.trophyID);
            }
            
            public String toString() {
                return "GameJoltTrophies.TrophiesAddAchievedRequest.TrophiesAddAchievedRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ", trophyID=" + this.trophyID + ")";
            }
        }
    }
    
    /**
     * The result of adding an achieved trophy.
     */
    public static class TrophiesAddAchievedValue implements GameJoltValue {
        
        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;
        
        /**
         * The request that triggered the response.
         */
        public GameJoltRequest request;
        
        /**
         * Whether the request succeeded or failed.
         */
        public boolean success;
        
        /**
         * If the request was not successful, this contains the error message.
         */
        public String message;
        
        TrophiesAddAchievedValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static TrophiesAddAchievedValueBuilder builder() {
            return new TrophiesAddAchievedValueBuilder();
        }
        
        public JsonValue getJsonValue() {
            return this.jsonValue;
        }
        
        public GameJoltRequest getRequest() {
            return this.request;
        }
        
        public boolean isSuccess() {
            return this.success;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public void setJsonValue(JsonValue jsonValue) {
            this.jsonValue = jsonValue;
        }
        
        public void setRequest(GameJoltRequest request) {
            this.request = request;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public static class TrophiesAddAchievedValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            TrophiesAddAchievedValueBuilder() {
            }
            
            public TrophiesAddAchievedValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public TrophiesAddAchievedValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public TrophiesAddAchievedValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public TrophiesAddAchievedValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public TrophiesAddAchievedValue build() {
                return new TrophiesAddAchievedValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltTrophies.TrophiesAddAchievedValue.TrophiesAddAchievedValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
    }
    
    /**
     * Listener for {@link TrophiesAddAchievedRequest}. Override
     * {@link TrophiesAddAchievedListener#trophiesAddAchieved(TrophiesAddAchievedValue)} to handle the server response.
     */
    public static abstract class TrophiesAddAchievedListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof TrophiesAddAchievedValue) trophiesAddAchieved((TrophiesAddAchievedValue) value);
        }
        
        public abstract void trophiesAddAchieved(TrophiesAddAchievedValue value);
    }
    
    /**
     * Remove a previously achieved trophy for a particular user.
     */
    public static class TrophiesRemoveAchievedRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The user's username. Required.
         */
        @NonNull
        private String username;
        
        /**
         * The user's token. Required.
         */
        @NonNull
        private String userToken;
        
        /**
         * The ID of the trophy to remove from the user. Required.
         */
        @NonNull
        private Integer trophyID;
        
        TrophiesRemoveAchievedRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken,
                                      @NonNull Integer trophyID) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
            this.trophyID = trophyID;
        }
        
        public static TrophiesRemoveAchievedRequestBuilder builder() {
            return new TrophiesRemoveAchievedRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            String builder = "/trophies/remove-achieved/?game_id=" + gameID +
                    "&username=" + urlEncode(username) +
                    "&user_token=" + urlEncode(userToken) +
                    "&trophy_id=" + trophyID;
            
            return builder;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link TrophiesRemoveAchievedValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link TrophiesRemoveAchievedValue} with the values returned from the server.
         */
        @Override
        public TrophiesRemoveAchievedValue handleResponse(JsonValue jsonValue) {
            return TrophiesRemoveAchievedValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getUsername() {
            return this.username;
        }
        
        public @NonNull String getUserToken() {
            return this.userToken;
        }
        
        public @NonNull Integer getTrophyID() {
            return this.trophyID;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setUsername(@NonNull String username) {
            this.username = username;
        }
        
        public void setUserToken(@NonNull String userToken) {
            this.userToken = userToken;
        }
        
        public void setTrophyID(@NonNull Integer trophyID) {
            this.trophyID = trophyID;
        }
        
        public static class TrophiesRemoveAchievedRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            private @NonNull Integer trophyID;
            
            TrophiesRemoveAchievedRequestBuilder() {
            }
            
            public TrophiesRemoveAchievedRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public TrophiesRemoveAchievedRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public TrophiesRemoveAchievedRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public TrophiesRemoveAchievedRequestBuilder trophyID(@NonNull Integer trophyID) {
                this.trophyID = trophyID;
                return this;
            }
            
            public TrophiesRemoveAchievedRequest build() {
                return new TrophiesRemoveAchievedRequest(this.gameID, this.username, this.userToken, this.trophyID);
            }
            
            public String toString() {
                return "GameJoltTrophies.TrophiesRemoveAchievedRequest.TrophiesRemoveAchievedRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ", trophyID=" + this.trophyID + ")";
            }
        }
    }
    
    /**
     * The result of removing a previously achieved trophy.
     */
    public static class TrophiesRemoveAchievedValue implements GameJoltValue {
        
        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;
        
        /**
         * The request that triggered the response.
         */
        public GameJoltRequest request;
        
        /**
         * Whether the request succeeded or failed.
         */
        public boolean success;
        
        /**
         * If the request was not successful, this contains the error message.
         */
        public String message;
        
        TrophiesRemoveAchievedValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static TrophiesRemoveAchievedValueBuilder builder() {
            return new TrophiesRemoveAchievedValueBuilder();
        }
        
        public JsonValue getJsonValue() {
            return this.jsonValue;
        }
        
        public GameJoltRequest getRequest() {
            return this.request;
        }
        
        public boolean isSuccess() {
            return this.success;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public void setJsonValue(JsonValue jsonValue) {
            this.jsonValue = jsonValue;
        }
        
        public void setRequest(GameJoltRequest request) {
            this.request = request;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public static class TrophiesRemoveAchievedValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            TrophiesRemoveAchievedValueBuilder() {
            }
            
            public TrophiesRemoveAchievedValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public TrophiesRemoveAchievedValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public TrophiesRemoveAchievedValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public TrophiesRemoveAchievedValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public TrophiesRemoveAchievedValue build() {
                return new TrophiesRemoveAchievedValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltTrophies.TrophiesRemoveAchievedValue.TrophiesRemoveAchievedValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
    }
    
    /**
     * Listener for {@link TrophiesRemoveAchievedRequest}. Override
     * {@link TrophiesRemoveAchievedListener#trophiesRemoveAchieved(TrophiesRemoveAchievedValue)} to handle the server
     * response.
     */
    public static abstract class TrophiesRemoveAchievedListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof TrophiesRemoveAchievedValue)
                trophiesRemoveAchieved((TrophiesRemoveAchievedValue) value);
        }
        
        public abstract void trophiesRemoveAchieved(TrophiesRemoveAchievedValue value);
    }
}
