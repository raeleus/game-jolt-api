package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.JsonValue;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
            String builder = "/sessions/open/?game_id=" + gameID +
                    "&username=" + urlEncode(username) +
                    "&user_token=" + urlEncode(userToken);
            
            return builder;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link SessionsOpenValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link SessionsOpenValue} with the values returned from the server.
         */
        @Override
        public SessionsOpenValue handleResponse(JsonValue jsonValue) {
            return SessionsOpenValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
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
    public static class SessionsOpenValue implements GameJoltValue {
        
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
    }
    
    /**
     * Listener for {@link SessionsOpenRequest}. Override {@link SessionsOpenListener#sessionsOpen(SessionsOpenValue)}
     * to handle the server response.
     */
    public static abstract class SessionsOpenListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof SessionsOpenValue) sessionsOpen((SessionsOpenValue) value);
        }
        
        public abstract void sessionsOpen(SessionsOpenValue value);
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
            String builder = "/sessions/ping/?game_id=" + gameID +
                    "&username=" + urlEncode(username) +
                    "&user_token=" + urlEncode(userToken);
            
            return builder;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link SessionsPingValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link SessionsPingValue} with the values returned from the server.
         */
        @Override
        public SessionsPingValue handleResponse(JsonValue jsonValue) {
            return SessionsPingValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
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
    public static class SessionsPingValue implements GameJoltValue {
        
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
    }
    
    /**
     * Listener for {@link SessionsPingRequest}. Override {@link SessionsPingListener#sessionsPing(SessionsPingValue)}
     * to handle the server response.
     */
    public static abstract class SessionsPingListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof SessionsPingValue) sessionsPing((SessionsPingValue) value);
        }
        
        public abstract void sessionsPing(SessionsPingValue value);
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
            String builder = "/sessions/check/?game_id=" + gameID +
                    "&username=" + urlEncode(username) +
                    "&user_token=" + urlEncode(userToken);
            
            return builder;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link SessionsCheckValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link SessionsCheckValue} with the values returned from the server.
         */
        @Override
        public SessionsCheckValue handleResponse(JsonValue jsonValue) {
            return SessionsCheckValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
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
    public static class SessionsCheckValue implements GameJoltValue {
        
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
    }
    
    /**
     * Listener for {@link SessionsCheckRequest}. Override
     * {@link SessionsCheckListener#sessionsCheck(SessionsCheckValue)} to handle the server response.
     */
    public static abstract class SessionsCheckListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof SessionsCheckValue) sessionsCheck((SessionsCheckValue) value);
        }
        
        public abstract void sessionsCheck(SessionsCheckValue value);
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
            String builder = "/sessions/close/?game_id=" + gameID +
                    "&username=" + urlEncode(username) +
                    "&user_token=" + urlEncode(userToken);
            
            return builder;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link SessionsCloseValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link SessionsCloseValue} with the values returned from the server.
         */
        @Override
        public SessionsCloseValue handleResponse(JsonValue jsonValue) {
            return SessionsCloseValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
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
    public static class SessionsCloseValue implements GameJoltValue {
        
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
    }
    
    /**
     * Listener for {@link SessionsCloseRequest}. Override
     * {@link SessionsCloseListener#sessionsClose(SessionsCloseValue)} to handle the server response.
     */
    public static abstract class SessionsCloseListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof SessionsCloseValue) sessionsClose((SessionsCloseValue) value);
        }
        
        public abstract void sessionsClose(SessionsCloseValue value);
    }
}
