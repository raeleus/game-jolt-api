package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import lombok.NonNull;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * Game Jolt supports multiple online score tables, or scoreboards, per game. You are able to, for example, have a score
 * table for each level in your game, or a table for different scoring metrics. Gamers will keep coming back to try to
 * achieve the highest scores for your game.
 * <p>
 * With multiple formatting and sorting options, the system is quite flexible. You are also able to include extra data
 * with each score. If there is other data associated with the score such as time played, coins collected, etc., you
 * should definitely include it. It will be helpful in cases where you believe a gamer has illegitimately achieved a
 * high score. Extra data you include is not shown anywhere on the site, and you are limited only by your own
 * imagination!
 */
public class GameJoltScores {
    /**
     * Adds a score for a user or guest. You can either store a score for a user or a guest. If you're storing for a
     * user, you must pass in the {@link ScoresAddRequest#setUsername(String)} and
     * {@link ScoresAddRequest#setUserToken(String)} parameters. If you're storing for a guest, you must pass in the
     * {@link ScoresAddRequest#setGuest(String)} parameter.
     * <p>
     * The {@link ScoresAddRequest#setExtraData(String)} value is only retrievable through the API and your game's
     * dashboard. It's never displayed publicly to users on the site. If there is other data associated with the score
     * such as time played, coins collected, etc., you should definitely include it. It will be helpful in cases where
     * you believe a gamer has illegitimately achieved a high score.
     * <p>
     * If {@link ScoresAddRequest#setTableID(Integer)} is left as null, the score will be submitted to the primary high
     * score table.
     */
    public static class ScoresAddRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The user's username. Optional.
         */
        private String username;
        
        /**
         * The user's token. Optional.
         */
        private String userToken;
        
        /**
         * The guest's name. Optional.
         */
        private String guest;
        
        /**
         * This is a string value associated with the score. Required.
         */
        @NonNull
        private String score;
        
        /**
         * This is a numerical sorting value associated with the score. All sorting will be based on this number.
         * Required.
         */
        @NonNull
        private Long sort;
        
        /**
         * If there's any extra data you would like to store as a string, you can use this variable. Optional.
         */
        private String extraData;
        
        /**
         * The ID of the score table to submit to. Optional.
         */
        private Integer tableID;
        
        ScoresAddRequest(@NonNull String gameID, String username, String userToken, String guest, @NonNull String score,
                         @NonNull Long sort, String extraData, Integer tableID) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
            this.guest = guest;
            this.score = score;
            this.sort = sort;
            this.extraData = extraData;
            this.tableID = tableID;
        }
        
        public static ScoresAddRequestBuilder builder() {
            return new ScoresAddRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/scores/add/?game_id=").append(gameID);
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));
            if (guest != null) builder.append("&guest=").append(urlEncode(guest));
            builder.append("&score=").append(urlEncode(score));
            builder.append("&sort=").append(sort);
            if (extraData != null) builder.append("&extra_data=").append(urlEncode(extraData));
            if (tableID != null) builder.append("&table_id=").append(tableID);
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link ScoresAddValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link ScoresAddValue} with the values returned from the server.
         */
        @Override
        public ScoresAddValue handleResponse(JsonValue jsonValue) {
            return ScoresAddValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public String getUserToken() {
            return this.userToken;
        }
        
        public String getGuest() {
            return this.guest;
        }
        
        public @NonNull String getScore() {
            return this.score;
        }
        
        public @NonNull Long getSort() {
            return this.sort;
        }
        
        public String getExtraData() {
            return this.extraData;
        }
        
        public Integer getTableID() {
            return this.tableID;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }
        
        public void setGuest(String guest) {
            this.guest = guest;
        }
        
        public void setScore(@NonNull String score) {
            this.score = score;
        }
        
        public void setSort(@NonNull Long sort) {
            this.sort = sort;
        }
        
        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }
        
        public void setTableID(Integer tableID) {
            this.tableID = tableID;
        }
        
        public static class ScoresAddRequestBuilder {
            private @NonNull String gameID;
            private String username;
            private String userToken;
            private String guest;
            private @NonNull String score;
            private @NonNull Long sort;
            private String extraData;
            private Integer tableID;
            
            ScoresAddRequestBuilder() {
            }
            
            public ScoresAddRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public ScoresAddRequestBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public ScoresAddRequestBuilder userToken(String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public ScoresAddRequestBuilder guest(String guest) {
                this.guest = guest;
                return this;
            }
            
            public ScoresAddRequestBuilder score(@NonNull String score) {
                this.score = score;
                return this;
            }
            
            public ScoresAddRequestBuilder sort(@NonNull Long sort) {
                this.sort = sort;
                return this;
            }
            
            public ScoresAddRequestBuilder extraData(String extraData) {
                this.extraData = extraData;
                return this;
            }
            
            public ScoresAddRequestBuilder tableID(Integer tableID) {
                this.tableID = tableID;
                return this;
            }
            
            public ScoresAddRequest build() {
                return new ScoresAddRequest(this.gameID, this.username, this.userToken, this.guest, this.score,
                        this.sort, this.extraData, this.tableID);
            }
            
            public String toString() {
                return "GameJoltScores.ScoresAddRequest.ScoresAddRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ", guest=" + this.guest + ", score=" + this.score + ", sort=" + this.sort + ", extraData=" + this.extraData + ", tableID=" + this.tableID + ")";
            }
        }
    }
    
    /**
     * The result of adding a score.
     */
    public static class ScoresAddValue implements GameJoltValue {
        
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
        
        ScoresAddValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static ScoresAddValueBuilder builder() {
            return new ScoresAddValueBuilder();
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
        
        public static class ScoresAddValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            ScoresAddValueBuilder() {
            }
            
            public ScoresAddValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public ScoresAddValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public ScoresAddValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public ScoresAddValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public ScoresAddValue build() {
                return new ScoresAddValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltScores.ScoresAddValue.ScoresAddValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
    }
    
    /**
     * Listener for {@link ScoresAddRequest}. Override {@link ScoresAddListener#scoresAdd(ScoresAddValue)} to handle the
     * server response.
     */
    public static abstract class ScoresAddListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof ScoresAddValue) scoresAdd((ScoresAddValue) value);
        }
        
        public abstract void scoresAdd(ScoresAddValue value);
    }
    
    /**
     * Returns the rank of a particular score on a score table. If {@link ScoresGetRankRequest#setTableID(Integer)} is
     * left blank, the ranks from the primary high score table will be returned.
     * <p>
     * If the score is not represented by any rank on the score table, the request will return the rank that is closest
     * to the requested score.
     */
    public static class ScoresGetRankRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * This is a numerical sorting value that is represented by a rank on the score table. Required.
         */
        @NonNull
        private Long sort;
        
        /**
         * The ID of the score table from which you want to get the rank. Optional.
         */
        private Integer tableID;
        
        ScoresGetRankRequest(@NonNull String gameID, @NonNull Long sort, Integer tableID) {
            this.gameID = gameID;
            this.sort = sort;
            this.tableID = tableID;
        }
        
        public static ScoresGetRankRequestBuilder builder() {
            return new ScoresGetRankRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/scores/get-rank/?game_id=").append(gameID);
            builder.append("&sort=").append(sort);
            if (tableID != null) builder.append("&table_id=").append(tableID);
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link ScoresGetRankValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link ScoresGetRankValue} with the values returned from the server.
         */
        @Override
        public ScoresGetRankValue handleResponse(JsonValue jsonValue) {
            return ScoresGetRankValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .rank(jsonValue.getInt("rank"))
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull Long getSort() {
            return this.sort;
        }
        
        public Integer getTableID() {
            return this.tableID;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setSort(@NonNull Long sort) {
            this.sort = sort;
        }
        
        public void setTableID(Integer tableID) {
            this.tableID = tableID;
        }
        
        public static class ScoresGetRankRequestBuilder {
            private @NonNull String gameID;
            private @NonNull Long sort;
            private Integer tableID;
            
            ScoresGetRankRequestBuilder() {
            }
            
            public ScoresGetRankRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public ScoresGetRankRequestBuilder sort(@NonNull Long sort) {
                this.sort = sort;
                return this;
            }
            
            public ScoresGetRankRequestBuilder tableID(Integer tableID) {
                this.tableID = tableID;
                return this;
            }
            
            public ScoresGetRankRequest build() {
                return new ScoresGetRankRequest(this.gameID, this.sort, this.tableID);
            }
            
            public String toString() {
                return "GameJoltScores.ScoresGetRankRequest.ScoresGetRankRequestBuilder(gameID=" + this.gameID + ", sort=" + this.sort + ", tableID=" + this.tableID + ")";
            }
        }
    }
    
    /**
     * The result of getting the rank of a score.
     */
    public static class ScoresGetRankValue implements GameJoltValue {
        
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
         * The rank of the score on the score table.
         */
        public int rank;
        
        ScoresGetRankValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message, int rank) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.rank = rank;
        }
        
        public static ScoresGetRankValueBuilder builder() {
            return new ScoresGetRankValueBuilder();
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
        
        public int getRank() {
            return this.rank;
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
        
        public void setRank(int rank) {
            this.rank = rank;
        }
        
        public static class ScoresGetRankValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private int rank;
            
            ScoresGetRankValueBuilder() {
            }
            
            public ScoresGetRankValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public ScoresGetRankValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public ScoresGetRankValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public ScoresGetRankValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public ScoresGetRankValueBuilder rank(int rank) {
                this.rank = rank;
                return this;
            }
            
            public ScoresGetRankValue build() {
                return new ScoresGetRankValue(this.jsonValue, this.request, this.success, this.message, this.rank);
            }
            
            public String toString() {
                return "GameJoltScores.ScoresGetRankValue.ScoresGetRankValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", rank=" + this.rank + ")";
            }
        }
    }
    
    /**
     * Listener for {@link ScoresGetRankRequest}. Override
     * {@link ScoresGetRankListener#scoresGetRank(ScoresGetRankValue)} to handle the server response.
     */
    public static abstract class ScoresGetRankListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof ScoresGetRankValue) scoresGetRank((ScoresGetRankValue) value);
        }
        
        public abstract void scoresGetRank(ScoresGetRankValue value);
    }
    
    /**
     * Returns a list of scores either for a user or globally for a game. If
     * {@link ScoresFetchRequest#setTableID(Integer)} is set to null, the scores from the primary score table will be
     * returned.
     * <p>
     * Only pass in the {@link ScoresFetchRequest#setUsername(String)} and
     * {@link ScoresFetchRequest#setUserToken(String)} if you would like to retrieve scores for just that user. Leave
     * the user information blank to retrieve all scores.
     * <p>
     * {@link ScoresFetchRequest#setGuest(String)} allows you to fetch scores by a specific guest name. Only pass either
     * the username/user_token pair or the guest (or none), never both.
     * <p>
     * Scores are returned in the order of the score table's sorting direction. e.g. for descending tables the bigger
     * scores are returned first.
     */
    public static class ScoresFetchRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        /**
         * The number of scores you'd like to return. The default value is 10 scores. The maximum amount of scores you
         * can retrieve is 100. Optional.
         */
        private Integer limit;
        
        /**
         * The ID of the score table. Optional.
         */
        private Integer tableID;
        
        /**
         * The user's username. Optional.
         */
        private String username;
        
        /**
         * The user's token. Optional.
         */
        private String userToken;
        
        /**
         * A guest's name. Optional.
         */
        private String guest;
        
        /**
         * Fetch only scores better than this score sort value. Optional.
         */
        private Long betterThan;
        
        /**
         * Fetch only scores worse than this score sort value. Optional.
         */
        private Long worseThan;
        
        ScoresFetchRequest(@NonNull String gameID, Integer limit, Integer tableID, String username, String userToken,
                           String guest, Long betterThan, Long worseThan) {
            this.gameID = gameID;
            this.limit = limit;
            this.tableID = tableID;
            this.username = username;
            this.userToken = userToken;
            this.guest = guest;
            this.betterThan = betterThan;
            this.worseThan = worseThan;
        }
        
        public static ScoresFetchRequestBuilder builder() {
            return new ScoresFetchRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/scores/?game_id=").append(gameID);
            if (limit != null) builder.append("&limit=").append(limit);
            if (tableID != null) builder.append("&table_id=").append(tableID);
            if (username != null) builder.append("&username=").append(urlEncode(username));
            if (userToken != null) builder.append("&user_token=").append(urlEncode(userToken));
            if (guest != null) builder.append("&guest=").append(urlEncode(guest));
            if (betterThan != null) builder.append("&better_than=").append(betterThan);
            if (worseThan != null) builder.append("&worse_than=").append(worseThan);
            
            return builder.toString();
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link ScoresFetchValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link ScoresFetchValue} with the values returned from the server.
         */
        @Override
        public ScoresFetchValue handleResponse(JsonValue jsonValue) {
            
            var scores = new Array<GameJoltScore>();
            var scoresJsonValue = jsonValue.get("scores");
            if (scoresJsonValue != null) for (JsonValue scoreJsonValue : scoresJsonValue.iterator()) {
                var score = GameJoltScore.builder()
                        .score(scoreJsonValue.getString("score"))
                        .sort(scoreJsonValue.getLong("sort"))
                        .extraData(scoreJsonValue.getString("extra_data"))
                        .user(scoreJsonValue.getString("user"))
                        .userID(scoreJsonValue.getString("user_id"))
                        .guest(scoreJsonValue.getString("guest"))
                        .stored(scoreJsonValue.getString("stored"))
                        .storedTimestamp(scoreJsonValue.getLong("stored_timestamp"))
                        .build();
                scores.add(score);
            }
            
            return ScoresFetchValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .scores(scores)
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public Integer getLimit() {
            return this.limit;
        }
        
        public Integer getTableID() {
            return this.tableID;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public String getUserToken() {
            return this.userToken;
        }
        
        public String getGuest() {
            return this.guest;
        }
        
        public Long getBetterThan() {
            return this.betterThan;
        }
        
        public Long getWorseThan() {
            return this.worseThan;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public void setLimit(Integer limit) {
            this.limit = limit;
        }
        
        public void setTableID(Integer tableID) {
            this.tableID = tableID;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }
        
        public void setGuest(String guest) {
            this.guest = guest;
        }
        
        public void setBetterThan(Long betterThan) {
            this.betterThan = betterThan;
        }
        
        public void setWorseThan(Long worseThan) {
            this.worseThan = worseThan;
        }
        
        public static class ScoresFetchRequestBuilder {
            private @NonNull String gameID;
            private Integer limit;
            private Integer tableID;
            private String username;
            private String userToken;
            private String guest;
            private Long betterThan;
            private Long worseThan;
            
            ScoresFetchRequestBuilder() {
            }
            
            public ScoresFetchRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public ScoresFetchRequestBuilder limit(Integer limit) {
                this.limit = limit;
                return this;
            }
            
            public ScoresFetchRequestBuilder tableID(Integer tableID) {
                this.tableID = tableID;
                return this;
            }
            
            public ScoresFetchRequestBuilder username(String username) {
                this.username = username;
                return this;
            }
            
            public ScoresFetchRequestBuilder userToken(String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public ScoresFetchRequestBuilder guest(String guest) {
                this.guest = guest;
                return this;
            }
            
            public ScoresFetchRequestBuilder betterThan(Long betterThan) {
                this.betterThan = betterThan;
                return this;
            }
            
            public ScoresFetchRequestBuilder worseThan(Long worseThan) {
                this.worseThan = worseThan;
                return this;
            }
            
            public ScoresFetchRequest build() {
                return new ScoresFetchRequest(this.gameID, this.limit, this.tableID, this.username, this.userToken,
                        this.guest, this.betterThan, this.worseThan);
            }
            
            public String toString() {
                return "GameJoltScores.ScoresFetchRequest.ScoresFetchRequestBuilder(gameID=" + this.gameID + ", limit=" + this.limit + ", tableID=" + this.tableID + ", username=" + this.username + ", userToken=" + this.userToken + ", guest=" + this.guest + ", betterThan=" + this.betterThan + ", worseThan=" + this.worseThan + ")";
            }
        }
    }
    
    /**
     * The result of fetching scores.
     */
    public static class ScoresFetchValue implements GameJoltValue {
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
         * All the scores returned by the request. They can occur multiple times.
         */
        public Array<GameJoltScore> scores;
        
        ScoresFetchValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message,
                         Array<GameJoltScore> scores) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.scores = scores;
        }
        
        public static ScoresFetchValueBuilder builder() {
            return new ScoresFetchValueBuilder();
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
        
        public Array<GameJoltScore> getScores() {
            return this.scores;
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
        
        public void setScores(Array<GameJoltScore> scores) {
            this.scores = scores;
        }
        
        public static class ScoresFetchValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private Array<GameJoltScore> scores;
            
            ScoresFetchValueBuilder() {
            }
            
            public ScoresFetchValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public ScoresFetchValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public ScoresFetchValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public ScoresFetchValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public ScoresFetchValueBuilder scores(Array<GameJoltScore> scores) {
                this.scores = scores;
                return this;
            }
            
            public ScoresFetchValue build() {
                return new ScoresFetchValue(this.jsonValue, this.request, this.success, this.message, this.scores);
            }
            
            public String toString() {
                return "GameJoltScores.ScoresFetchValue.ScoresFetchValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", scores=" + this.scores + ")";
            }
        }
    }
    
    /**
     * Listener for {@link ScoresFetchRequest}. Override {@link ScoresFetchListener#scoresFetch(ScoresFetchValue)} to
     * handle the server response.
     */
    public static abstract class ScoresFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof ScoresFetchValue) scoresFetch((ScoresFetchValue) value);
        }
        
        public abstract void scoresFetch(ScoresFetchValue value);
    }
    
    /**
     * An individual score as returned by using {@link ScoresFetchValue#getScores()}
     */
    public static class GameJoltScore {
        
        /**
         * The score string.
         */
        public String score;
        
        /**
         * The score's numerical sort value.
         */
        public long sort;
        
        /**
         * Any extra data associated with the score.
         */
        public String extraData;
        
        /**
         * If this is a user score, this is the display name for the user.
         */
        public String user;
        
        /**
         * If this is a user score, this is the user's ID.
         */
        public String userID;
        
        /**
         * If this is a guest score, this is the guest's submitted name.
         */
        public String guest;
        
        /**
         * Returns when the score was logged by the user.
         */
        public String stored;
        
        /**
         * Returns the timestamp (in seconds) of when the score was logged by the user.
         */
        public long storedTimestamp;
        
        GameJoltScore(String score, long sort, String extraData, String user, String userID, String guest,
                      String stored, long storedTimestamp) {
            this.score = score;
            this.sort = sort;
            this.extraData = extraData;
            this.user = user;
            this.userID = userID;
            this.guest = guest;
            this.stored = stored;
            this.storedTimestamp = storedTimestamp;
        }
        
        public static GameJoltScoreBuilder builder() {
            return new GameJoltScoreBuilder();
        }
        
        public String getScore() {
            return this.score;
        }
        
        public long getSort() {
            return this.sort;
        }
        
        public String getExtraData() {
            return this.extraData;
        }
        
        public String getUser() {
            return this.user;
        }
        
        public String getUserID() {
            return this.userID;
        }
        
        public String getGuest() {
            return this.guest;
        }
        
        public String getStored() {
            return this.stored;
        }
        
        public long getStoredTimestamp() {
            return this.storedTimestamp;
        }
        
        public void setScore(String score) {
            this.score = score;
        }
        
        public void setSort(long sort) {
            this.sort = sort;
        }
        
        public void setExtraData(String extraData) {
            this.extraData = extraData;
        }
        
        public void setUser(String user) {
            this.user = user;
        }
        
        public void setUserID(String userID) {
            this.userID = userID;
        }
        
        public void setGuest(String guest) {
            this.guest = guest;
        }
        
        public void setStored(String stored) {
            this.stored = stored;
        }
        
        public void setStoredTimestamp(long storedTimestamp) {
            this.storedTimestamp = storedTimestamp;
        }
        
        public String toString() {
            return "GameJoltScores.GameJoltScore(score=" + this.getScore() + ", sort=" + this.getSort() + ", extraData=" + this.getExtraData() + ", user=" + this.getUser() + ", userID=" + this.getUserID() + ", guest=" + this.getGuest() + ", stored=" + this.getStored() + ", storedTimestamp=" + this.getStoredTimestamp() + ")";
        }
        
        public static class GameJoltScoreBuilder {
            private String score;
            private long sort;
            private String extraData;
            private String user;
            private String userID;
            private String guest;
            private String stored;
            private long storedTimestamp;
            
            GameJoltScoreBuilder() {
            }
            
            public GameJoltScoreBuilder score(String score) {
                this.score = score;
                return this;
            }
            
            public GameJoltScoreBuilder sort(long sort) {
                this.sort = sort;
                return this;
            }
            
            public GameJoltScoreBuilder extraData(String extraData) {
                this.extraData = extraData;
                return this;
            }
            
            public GameJoltScoreBuilder user(String user) {
                this.user = user;
                return this;
            }
            
            public GameJoltScoreBuilder userID(String userID) {
                this.userID = userID;
                return this;
            }
            
            public GameJoltScoreBuilder guest(String guest) {
                this.guest = guest;
                return this;
            }
            
            public GameJoltScoreBuilder stored(String stored) {
                this.stored = stored;
                return this;
            }
            
            public GameJoltScoreBuilder storedTimestamp(long storedTimestamp) {
                this.storedTimestamp = storedTimestamp;
                return this;
            }
            
            public GameJoltScore build() {
                return new GameJoltScore(this.score, this.sort, this.extraData, this.user, this.userID, this.guest,
                        this.stored, this.storedTimestamp);
            }
            
            public String toString() {
                return "GameJoltScores.GameJoltScore.GameJoltScoreBuilder(score=" + this.score + ", sort=" + this.sort + ", extraData=" + this.extraData + ", user=" + this.user + ", userID=" + this.userID + ", guest=" + this.guest + ", stored=" + this.stored + ", storedTimestamp=" + this.storedTimestamp + ")";
            }
        }
    }
    
    /**
     * Returns a list of high score tables for a game.
     */
    public static class ScoresTablesRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;
        
        ScoresTablesRequest(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public static ScoresTablesRequestBuilder builder() {
            return new ScoresTablesRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            
            return "/scores/tables/?game_id=" + gameID;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link ScoresTablesValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link ScoresTablesValue} with the values returned from the server.
         */
        @Override
        public ScoresTablesValue handleResponse(JsonValue jsonValue) {
            
            var tables = new Array<GameJoltTable>();
            var tablesJsonValue = jsonValue.get("tables");
            for (JsonValue tableJsonValue : tablesJsonValue.iterator()) {
                var table = GameJoltTable.builder()
                        .id(tableJsonValue.getInt("id"))
                        .name(tableJsonValue.getString("name"))
                        .description(tableJsonValue.getString("description"))
                        .primary(tableJsonValue.getBoolean("primary"))
                        .build();
                tables.add(table);
            }
            
            return ScoresTablesValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .tables(tables)
                    .build();
        }
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public void setGameID(@NonNull String gameID) {
            this.gameID = gameID;
        }
        
        public static class ScoresTablesRequestBuilder {
            private @NonNull String gameID;
            
            ScoresTablesRequestBuilder() {
            }
            
            public ScoresTablesRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public ScoresTablesRequest build() {
                return new ScoresTablesRequest(this.gameID);
            }
            
            public String toString() {
                return "GameJoltScores.ScoresTablesRequest.ScoresTablesRequestBuilder(gameID=" + this.gameID + ")";
            }
        }
    }
    
    /**
     * The list of high score tables.
     */
    public static class ScoresTablesValue implements GameJoltValue {
        
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
         * The high score tables of the game. They can occur multiple times.
         */
        public Array<GameJoltTable> tables;
        
        ScoresTablesValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message,
                          Array<GameJoltTable> tables) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.tables = tables;
        }
        
        public static ScoresTablesValueBuilder builder() {
            return new ScoresTablesValueBuilder();
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
        
        public Array<GameJoltTable> getTables() {
            return this.tables;
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
        
        public void setTables(Array<GameJoltTable> tables) {
            this.tables = tables;
        }
        
        public static class ScoresTablesValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private Array<GameJoltTable> tables;
            
            ScoresTablesValueBuilder() {
            }
            
            public ScoresTablesValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public ScoresTablesValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public ScoresTablesValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public ScoresTablesValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public ScoresTablesValueBuilder tables(Array<GameJoltTable> tables) {
                this.tables = tables;
                return this;
            }
            
            public ScoresTablesValue build() {
                return new ScoresTablesValue(this.jsonValue, this.request, this.success, this.message, this.tables);
            }
            
            public String toString() {
                return "GameJoltScores.ScoresTablesValue.ScoresTablesValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", tables=" + this.tables + ")";
            }
        }
    }
    
    /**
     * Listener for {@link ScoresTablesRequest}. Override {@link ScoresTablesListener#scoresTables(ScoresTablesValue)}
     * to handle the server response.
     */
    public static abstract class ScoresTablesListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof ScoresTablesValue) scoresTables((ScoresTablesValue) value);
        }
        
        public abstract void scoresTables(ScoresTablesValue value);
    }
    
    /**
     * The high score table value.
     */
    public static class GameJoltTable {
        
        /**
         * The ID of the score table.
         */
        public int id;
        
        /**
         * The developer-defined name of the score table.
         */
        public String name;
        
        /**
         * The developer-defined description of the score table.
         */
        public String description;
        
        /**
         * Whether or not this is the default score table. Scores are submitted to the primary table by default.
         */
        public boolean primary;
        
        GameJoltTable(int id, String name, String description, boolean primary) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.primary = primary;
        }
        
        public static GameJoltTableBuilder builder() {
            return new GameJoltTableBuilder();
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getDescription() {
            return this.description;
        }
        
        public boolean isPrimary() {
            return this.primary;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public void setPrimary(boolean primary) {
            this.primary = primary;
        }
        
        public String toString() {
            return "GameJoltScores.GameJoltTable(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", primary=" + this.isPrimary() + ")";
        }
        
        public static class GameJoltTableBuilder {
            private int id;
            private String name;
            private String description;
            private boolean primary;
            
            GameJoltTableBuilder() {
            }
            
            public GameJoltTableBuilder id(int id) {
                this.id = id;
                return this;
            }
            
            public GameJoltTableBuilder name(String name) {
                this.name = name;
                return this;
            }
            
            public GameJoltTableBuilder description(String description) {
                this.description = description;
                return this;
            }
            
            public GameJoltTableBuilder primary(boolean primary) {
                this.primary = primary;
                return this;
            }
            
            public GameJoltTable build() {
                return new GameJoltTable(this.id, this.name, this.description, this.primary);
            }
            
            public String toString() {
                return "GameJoltScores.GameJoltTable.GameJoltTableBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", primary=" + this.primary + ")";
            }
        }
    }
}
