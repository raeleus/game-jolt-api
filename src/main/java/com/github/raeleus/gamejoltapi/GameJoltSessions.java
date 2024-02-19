package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.JsonValue;
import lombok.NonNull;

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
        
        SessionsOpenRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static SessionsOpenRequestBuilder builder() {
            return new SessionsOpenRequestBuilder();
        }
        
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
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getUsername() {
            return this.username;
        }
        
        public @NonNull String getUserToken() {
            return this.userToken;
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
        
        public static class SessionsOpenRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            
            SessionsOpenRequestBuilder() {
            }
            
            public SessionsOpenRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public SessionsOpenRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public SessionsOpenRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public SessionsOpenRequest build() {
                return new SessionsOpenRequest(this.gameID, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltSessions.SessionsOpenRequest.SessionsOpenRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The result of opening a game session.
     */
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
        
        SessionsOpenValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static SessionsOpenValueBuilder builder() {
            return new SessionsOpenValueBuilder();
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
        
        public static class SessionsOpenValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            SessionsOpenValueBuilder() {
            }
            
            public SessionsOpenValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public SessionsOpenValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public SessionsOpenValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public SessionsOpenValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public SessionsOpenValue build() {
                return new SessionsOpenValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltSessions.SessionsOpenValue.SessionsOpenValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
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
        
        SessionsPingRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken,
                            SessionStatus status) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
            this.status = status;
        }
        
        public static SessionsPingRequestBuilder builder() {
            return new SessionsPingRequestBuilder();
        }
        
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
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getUsername() {
            return this.username;
        }
        
        public @NonNull String getUserToken() {
            return this.userToken;
        }
        
        public SessionStatus getStatus() {
            return this.status;
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
        
        public void setStatus(SessionStatus status) {
            this.status = status;
        }
        
        public static class SessionsPingRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            private SessionStatus status;
            
            SessionsPingRequestBuilder() {
            }
            
            public SessionsPingRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public SessionsPingRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public SessionsPingRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public SessionsPingRequestBuilder status(SessionStatus status) {
                this.status = status;
                return this;
            }
            
            public SessionsPingRequest build() {
                return new SessionsPingRequest(this.gameID, this.username, this.userToken, this.status);
            }
            
            public String toString() {
                return "GameJoltSessions.SessionsPingRequest.SessionsPingRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ", status=" + this.status + ")";
            }
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
        
        SessionsPingValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static SessionsPingValueBuilder builder() {
            return new SessionsPingValueBuilder();
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
        
        public static class SessionsPingValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            SessionsPingValueBuilder() {
            }
            
            public SessionsPingValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public SessionsPingValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public SessionsPingValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public SessionsPingValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public SessionsPingValue build() {
                return new SessionsPingValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltSessions.SessionsPingValue.SessionsPingValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
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
        
        SessionsCheckRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static SessionsCheckRequestBuilder builder() {
            return new SessionsCheckRequestBuilder();
        }
        
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
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getUsername() {
            return this.username;
        }
        
        public @NonNull String getUserToken() {
            return this.userToken;
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
        
        public static class SessionsCheckRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            
            SessionsCheckRequestBuilder() {
            }
            
            public SessionsCheckRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public SessionsCheckRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public SessionsCheckRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public SessionsCheckRequest build() {
                return new SessionsCheckRequest(this.gameID, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltSessions.SessionsCheckRequest.SessionsCheckRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The result of opening a session.
     */
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
        
        SessionsCheckValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static SessionsCheckValueBuilder builder() {
            return new SessionsCheckValueBuilder();
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
        
        public static class SessionsCheckValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            SessionsCheckValueBuilder() {
            }
            
            public SessionsCheckValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public SessionsCheckValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public SessionsCheckValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public SessionsCheckValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public SessionsCheckValue build() {
                return new SessionsCheckValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltSessions.SessionsCheckValue.SessionsCheckValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
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
        
        SessionsCloseRequest(@NonNull String gameID, @NonNull String username, @NonNull String userToken) {
            this.gameID = gameID;
            this.username = username;
            this.userToken = userToken;
        }
        
        public static SessionsCloseRequestBuilder builder() {
            return new SessionsCloseRequestBuilder();
        }
        
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
        
        public @NonNull String getGameID() {
            return this.gameID;
        }
        
        public @NonNull String getUsername() {
            return this.username;
        }
        
        public @NonNull String getUserToken() {
            return this.userToken;
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
        
        public static class SessionsCloseRequestBuilder {
            private @NonNull String gameID;
            private @NonNull String username;
            private @NonNull String userToken;
            
            SessionsCloseRequestBuilder() {
            }
            
            public SessionsCloseRequestBuilder gameID(@NonNull String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public SessionsCloseRequestBuilder username(@NonNull String username) {
                this.username = username;
                return this;
            }
            
            public SessionsCloseRequestBuilder userToken(@NonNull String userToken) {
                this.userToken = userToken;
                return this;
            }
            
            public SessionsCloseRequest build() {
                return new SessionsCloseRequest(this.gameID, this.username, this.userToken);
            }
            
            public String toString() {
                return "GameJoltSessions.SessionsCloseRequest.SessionsCloseRequestBuilder(gameID=" + this.gameID + ", username=" + this.username + ", userToken=" + this.userToken + ")";
            }
        }
    }
    
    /**
     * The result of closing the active session.
     */
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
        
        SessionsCloseValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
        }
        
        public static SessionsCloseValueBuilder builder() {
            return new SessionsCloseValueBuilder();
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
        
        public static class SessionsCloseValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            
            SessionsCloseValueBuilder() {
            }
            
            public SessionsCloseValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public SessionsCloseValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public SessionsCloseValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public SessionsCloseValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public SessionsCloseValue build() {
                return new SessionsCloseValue(this.jsonValue, this.request, this.success, this.message);
            }
            
            public String toString() {
                return "GameJoltSessions.SessionsCloseValue.SessionsCloseValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ")";
            }
        }
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
