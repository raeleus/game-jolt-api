package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.JsonValue;
import lombok.*;

import static com.github.raeleus.gamejoltapi.GameJoltApi.urlEncode;

/**
 * Sessions are used to tell Game Jolt when a user is playing a game, and what state they are in while playing (active
 * or idle).
 */
public class GameJoltSessions {
    /**
     * Opens a game session for a particular user and allows you to tell Game Jolt that a user is playing your game. You
     * must ping the session to keep it active and you must close it when you're done with it.
     * <p>
     * You can only have one open session for a user at a time. If you try to open a new session while one is running,
     * the system will close out the current one before opening the new one.
     */
    @Builder
    @Getter
    @Setter
    public static class SessionsOpenRequest implements GameJoltRequest {

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
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/sessions/open/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link SessionsOpenData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link SessionsOpenData} with the values returned from the server.
         */
        @Override
        public SessionsOpenData handleResponse(JsonValue jsonValue) {
            return SessionsOpenData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of opening a game session.
     */
    @Builder
    @Getter
    @Setter
    public static class SessionsOpenData implements GameJoltData {

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
     * Listener for {@link SessionsOpenRequest}. Override {@link SessionsOpenListener#sessionsOpen(JsonValue,
     * SessionsOpenData)} to handle the server response.
     */
    public static abstract class SessionsOpenListener extends GameJoltAdapter {
        @Override
        public void response(JsonValue jsonValue, GameJoltData data) {
            if (data instanceof SessionsOpenData) sessionsOpen(jsonValue, (SessionsOpenData) data);
        }

        public abstract void sessionsOpen(JsonValue jsonValue, SessionsOpenData data);
    }

    /**
     * Pings an open session to tell the system that it's still active. If the session hasn't been pinged within 120
     * seconds, the system will close the session and you will have to open another one. It's recommended that you ping
     * about every 30 seconds or so to keep the system from clearing out your session.
     * <p>
     * You can also let the system know whether the player is in an active or idle state within your game.
     */
    @Builder
    @Getter
    @Setter
    public static class SessionsPingRequest implements GameJoltRequest {

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
         * Sets the status of the session. Optional.
         */
        private SessionStatus status;

        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/sessions/ping/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link SessionsPingData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link SessionsPingData} with the values returned from the server.
         */
        @Override
        public SessionsPingData handleResponse(JsonValue jsonValue) {
            return SessionsPingData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    public enum SessionStatus {
        /**
         * Sets the session to the active state.
         */
        ACTIVE("active"),
        /**
         * Sets the session to the idle state.
         */
        IDLE("idle");

        public String name;

        SessionStatus(String name) {
            this.name = name;
        }
    }

    /**
     * The result of pinging the session.
     */
    @Builder
    @Getter
    @Setter
    public static class SessionsPingData implements GameJoltData {

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
     * Listener for {@link SessionsPingRequest}. Override {@link SessionsPingListener#sessionsPing(JsonValue,
     * SessionsPingData)} to handle the server response.
     */
    public static abstract class SessionsPingListener extends GameJoltAdapter {
        @Override
        public void response(JsonValue jsonValue, GameJoltData data) {
            if (data instanceof SessionsPingData) sessionsPing(jsonValue, (SessionsPingData) data);
        }

        public abstract void sessionsPing(JsonValue jsonValue, SessionsPingData data);
    }

    /**
     * Checks to see if there is an open session for the user. Can be used to see if a particular user account is active
     * in the game.
     */
    @Builder
    @Getter
    @Setter
    public static class SessionsCheckRequest implements GameJoltRequest {

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
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/sessions/check/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link SessionsCheckData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link SessionsCheckData} with the values returned from the server.
         */
        @Override
        public SessionsCheckData handleResponse(JsonValue jsonValue) {
            return SessionsCheckData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of opening a session.
     */
    @Builder
    @Getter
    @Setter
    public static class SessionsCheckData implements GameJoltData {

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
     * Listener for {@link SessionsCheckRequest}. Override {@link SessionsCheckListener#sessionsCheck(JsonValue,
     * SessionsCheckData)} to handle the server response.
     */
    public static abstract class SessionsCheckListener extends GameJoltAdapter {
        @Override
        public void response(JsonValue jsonValue, GameJoltData data) {
            if (data instanceof SessionsCheckData) sessionsCheck(jsonValue, (SessionsCheckData) data);
        }

        public abstract void sessionsCheck(JsonValue jsonValue, SessionsCheckData data);
    }

    /**
     * Closes the active session.
     */
    @Builder
    @Getter
    @Setter
    public static class SessionsCloseRequest implements GameJoltRequest {

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

        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("/sessions/close/?game_id=").append(gameID);
            builder.append("&username=").append(urlEncode(username));
            builder.append("&user_token=").append(urlEncode(userToken));

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link SessionsCloseData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link SessionsCloseData} with the values returned from the server.
         */
        @Override
        public SessionsCloseData handleResponse(JsonValue jsonValue) {
            return SessionsCloseData.builder()
                .jsonValue(jsonValue)
                .success(jsonValue.getBoolean("success"))
                .message(jsonValue.getString("message", null))
                .build();
        }
    }

    /**
     * The result of closing the active session.
     */
    @Builder
    @Getter
    @Setter
    public static class SessionsCloseData implements GameJoltData {

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
     * Listener for {@link SessionsCloseRequest}. Override {@link SessionsCloseListener#sessionsClose(JsonValue,
     * SessionsCloseData)} to handle the server response.
     */
    public static abstract class SessionsCloseListener extends GameJoltAdapter {
        @Override
        public void response(JsonValue jsonValue, GameJoltData data) {
            if (data instanceof SessionsCloseData) sessionsClose(jsonValue, (SessionsCloseData) data);
        }

        public abstract void sessionsClose(JsonValue jsonValue, SessionsCloseData data);
    }
}
