package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import lombok.*;

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
    @Builder
    @Getter
    @Setter
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
         * Handles the server JSON response and returns a corresponding {@link ScoresAddData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link ScoresAddData} with the values returned from the server.
         */
        @Override
        public ScoresAddData handleResponse(JsonValue jsonValue) {
            return ScoresAddData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of adding a score.
     */
    @Builder
    @Getter
    @Setter
    public static class ScoresAddData implements GameJoltData {

        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;

        /**
         * Whether the request succeeded or failed.
         */
        public boolean success;

        /**
         * If the request was not successful, this contains the error message.
         */
        public String message;
    }

    /**
     * Listener for {@link ScoresAddRequest}. Override {@link ScoresAddListener#scoresAdd(ScoresAddData)} to
     * handle the server response.
     */
    public static abstract class ScoresAddListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof ScoresAddData) scoresAdd((ScoresAddData) data);
        }

        public abstract void scoresAdd(ScoresAddData data);
    }

    /**
     * Returns the rank of a particular score on a score table. If {@link ScoresGetRankRequest#setTableID(Integer)} is
     * left blank, the ranks from the primary high score table will be returned.
     * <p>
     * If the score is not represented by any rank on the score table, the request will return the rank that is closest
     * to the requested score.
     */
    @Builder
    @Getter
    @Setter
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
         * Handles the server JSON response and returns a corresponding {@link ScoresGetRankData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link ScoresGetRankData} with the values returned from the server.
         */
        @Override
        public ScoresGetRankData handleResponse(JsonValue jsonValue) {
            return ScoresGetRankData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .rank(jsonValue.getInt("rank"))
                .build();
        }
    }

    /**
     * The result of getting the rank of a score.
     */
    @Builder
    @Getter
    @Setter
    public static class ScoresGetRankData implements GameJoltData {

        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;

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
    }

    /**
     * Listener for {@link ScoresGetRankRequest}. Override {@link ScoresGetRankListener#scoresGetRank(
     * ScoresGetRankData)} to handle the server response.
     */
    public static abstract class ScoresGetRankListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof ScoresGetRankData) scoresGetRank((ScoresGetRankData) data);
        }

        public abstract void scoresGetRank(ScoresGetRankData data);
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
    @Builder
    @Getter
    @Setter
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
         * Handles the server JSON response and returns a corresponding {@link ScoresFetchData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link ScoresFetchData} with the values returned from the server.
         */
        @Override
        public ScoresFetchData handleResponse(JsonValue jsonValue) {

            var scores = new Array<GameJoltScore>();
            var scoresJsonValue = jsonValue.get("scores");
            for (JsonValue scoreJsonValue : scoresJsonValue.iterator()) {
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

            return ScoresFetchData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .scores(scores)
                .build();
        }
    }

    /**
     * The result of fetching scores.
     */
    @Builder
    @Getter
    @Setter
    public static class ScoresFetchData implements GameJoltData {
        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;

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
    }

    /**
     * Listener for {@link ScoresFetchRequest}. Override {@link ScoresFetchListener#scoresFetch(
     * ScoresFetchData)} to handle the server response.
     */
    public static abstract class ScoresFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof ScoresFetchData) scoresFetch((ScoresFetchData) data);
        }

        public abstract void scoresFetch(ScoresFetchData data);
    }

    /**
     * An individual score as returned by using {@link ScoresFetchData#getScores()}
     */
    @Builder
    @Getter
    @Setter
    @ToString
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
    }

    /**
     * Returns a list of high score tables for a game.
     */
    @Builder
    @Getter
    @Setter
    public static class ScoresTablesRequest implements GameJoltRequest {

        /**
         * The ID of your game. Required.
         */
        @NonNull
        private String gameID;

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/scores/tables/?game_id=").append(gameID);

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link ScoresTablesData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link ScoresTablesData} with the values returned from the server.
         */
        @Override
        public ScoresTablesData handleResponse(JsonValue jsonValue) {

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

            return ScoresTablesData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .tables(tables)
                .build();
        }
    }

    /**
     * The list of high score tables.
     */
    @Builder
    @Getter
    @Setter
    public static class ScoresTablesData implements GameJoltData {

        /**
         * The JSON data from the server response.
         */
        public JsonValue jsonValue;

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
    }

    /**
     * Listener for {@link ScoresTablesRequest}. Override {@link ScoresTablesListener#scoresTables(ScoresTablesData)} to
     * handle the server response.
     */
    public static abstract class ScoresTablesListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof ScoresTablesData) scoresTables((ScoresTablesData) data);
        }

        public abstract void scoresTables(ScoresTablesData data);
    }

    /**
     * The high score table value.
     */
    @Builder
    @Getter
    @Setter
    @ToString
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
    }
}
