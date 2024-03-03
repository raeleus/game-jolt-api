package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.JsonValue;

/**
 * A namespace to obtain time information from the Game Jolt server. It can be used to realize real-time gameplay for
 * everyone playing the same game or security systems based on the server time.
 */
public class GameJoltTime {
    
    /**
     * Returns the time of the Game Jolt server.
     */
    public static class TimeFetchRequest implements GameJoltRequest {
        
        /**
         * The ID of your game. Required.
         */
        private String gameID;
        
        TimeFetchRequest( String gameID) {
            this.gameID = gameID;
        }
        
        public static TimeFetchRequestBuilder builder() {
            return new TimeFetchRequestBuilder();
        }
        
        /**
         * The url string defining this request. Note that it does not contain the base URL pointing to the Game Jolt
         * API.
         */
        @Override
        public String toString() {
            
            return "/time/?game_id=" + gameID;
        }
        
        /**
         * Handles the server JSON response and returns a corresponding {@link TimeFetchValue}.
         *
         * @param jsonValue The JSON response from the server.
         * @return The {@link TimeFetchValue} with the values returned from the server.
         */
        @Override
        public TimeFetchValue handleResponse(JsonValue jsonValue) {
            return TimeFetchValue.builder()
                    .jsonValue(jsonValue)
                    .request(this)
                    .success(jsonValue.getBoolean("success"))
                    .message(jsonValue.getString("message", null))
                    .timestamp(jsonValue.getLong("timestamp"))
                    .timezone(jsonValue.getString("timezone"))
                    .year(jsonValue.getInt("year"))
                    .month(jsonValue.getInt("month"))
                    .day(jsonValue.getInt("day"))
                    .hour(jsonValue.getInt("hour"))
                    .minute(jsonValue.getInt("minute"))
                    .second(jsonValue.getInt("second"))
                    .build();
        }
        
        public  String getGameID() {
            return this.gameID;
        }
        
        public void setGameID( String gameID) {
            this.gameID = gameID;
        }
        
        public static class TimeFetchRequestBuilder {
            private  String gameID;
            
            TimeFetchRequestBuilder() {
            }
            
            public TimeFetchRequestBuilder gameID( String gameID) {
                this.gameID = gameID;
                return this;
            }
            
            public TimeFetchRequest build() {
                return new TimeFetchRequest(this.gameID);
            }
            
            public String toString() {
                return "GameJoltTime.TimeFetchRequest.TimeFetchRequestBuilder(gameID=" + this.gameID + ")";
            }
        }
    }
    
    /**
     * The result of fetching the time from the server.
     */
    public static class TimeFetchValue implements GameJoltValue {
        
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
         * The UNIX time stamp (in seconds) representing the server's time.
         */
        public long timestamp;
        
        /**
         * The timezone of the server.
         */
        public String timezone;
        
        /**
         * The current year.
         */
        public int year;
        
        /**
         * The current month.
         */
        public int month;
        
        /**
         * The day of the month.
         */
        public int day;
        
        /**
         * The hour of the day. Returned as 24 hour time.
         */
        public int hour;
        
        /**
         * The minute of the hour.
         */
        public int minute;
        
        /**
         * The seconds of the minute.
         */
        public int second;
        
        TimeFetchValue(JsonValue jsonValue, GameJoltRequest request, boolean success, String message, long timestamp,
                       String timezone, int year, int month, int day, int hour, int minute, int second) {
            this.jsonValue = jsonValue;
            this.request = request;
            this.success = success;
            this.message = message;
            this.timestamp = timestamp;
            this.timezone = timezone;
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }
        
        public static TimeFetchValueBuilder builder() {
            return new TimeFetchValueBuilder();
        }
        
        private String formatMinute(int minute) {
            return (minute >= 10 ? "" : "0") + minute;
        }
        
        @Override
        public String toString() {
            int standardHour = hour == 0 || hour == 12 ? 12 : hour % 12;
            return month + "/" + day + "/" + year + " " + standardHour + ":" + formatMinute(
                    minute) + ":" + formatMinute(second) + (hour < 12 ? " AM" : " PM");
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
        
        public long getTimestamp() {
            return this.timestamp;
        }
        
        public String getTimezone() {
            return this.timezone;
        }
        
        public int getYear() {
            return this.year;
        }
        
        public int getMonth() {
            return this.month;
        }
        
        public int getDay() {
            return this.day;
        }
        
        public int getHour() {
            return this.hour;
        }
        
        public int getMinute() {
            return this.minute;
        }
        
        public int getSecond() {
            return this.second;
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
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
        
        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
        
        public void setYear(int year) {
            this.year = year;
        }
        
        public void setMonth(int month) {
            this.month = month;
        }
        
        public void setDay(int day) {
            this.day = day;
        }
        
        public void setHour(int hour) {
            this.hour = hour;
        }
        
        public void setMinute(int minute) {
            this.minute = minute;
        }
        
        public void setSecond(int second) {
            this.second = second;
        }
        
        public static class TimeFetchValueBuilder {
            private JsonValue jsonValue;
            private GameJoltRequest request;
            private boolean success;
            private String message;
            private long timestamp;
            private String timezone;
            private int year;
            private int month;
            private int day;
            private int hour;
            private int minute;
            private int second;
            
            TimeFetchValueBuilder() {
            }
            
            public TimeFetchValueBuilder jsonValue(JsonValue jsonValue) {
                this.jsonValue = jsonValue;
                return this;
            }
            
            public TimeFetchValueBuilder request(GameJoltRequest request) {
                this.request = request;
                return this;
            }
            
            public TimeFetchValueBuilder success(boolean success) {
                this.success = success;
                return this;
            }
            
            public TimeFetchValueBuilder message(String message) {
                this.message = message;
                return this;
            }
            
            public TimeFetchValueBuilder timestamp(long timestamp) {
                this.timestamp = timestamp;
                return this;
            }
            
            public TimeFetchValueBuilder timezone(String timezone) {
                this.timezone = timezone;
                return this;
            }
            
            public TimeFetchValueBuilder year(int year) {
                this.year = year;
                return this;
            }
            
            public TimeFetchValueBuilder month(int month) {
                this.month = month;
                return this;
            }
            
            public TimeFetchValueBuilder day(int day) {
                this.day = day;
                return this;
            }
            
            public TimeFetchValueBuilder hour(int hour) {
                this.hour = hour;
                return this;
            }
            
            public TimeFetchValueBuilder minute(int minute) {
                this.minute = minute;
                return this;
            }
            
            public TimeFetchValueBuilder second(int second) {
                this.second = second;
                return this;
            }
            
            public TimeFetchValue build() {
                return new TimeFetchValue(this.jsonValue, this.request, this.success, this.message, this.timestamp,
                        this.timezone, this.year, this.month, this.day, this.hour, this.minute, this.second);
            }
            
            public String toString() {
                return "GameJoltTime.TimeFetchValue.TimeFetchValueBuilder(jsonValue=" + this.jsonValue + ", request=" + this.request + ", success=" + this.success + ", message=" + this.message + ", timestamp=" + this.timestamp + ", timezone=" + this.timezone + ", year=" + this.year + ", month=" + this.month + ", day=" + this.day + ", hour=" + this.hour + ", minute=" + this.minute + ", second=" + this.second + ")";
            }
        }
    }
    
    /**
     * Listener for {@link TimeFetchRequest}. Override {@link TimeFetchListener#timeFetch(TimeFetchValue)} to handle the
     * server response.
     */
    public static abstract class TimeFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltRequest request, GameJoltValue value) {
            if (value instanceof TimeFetchValue) timeFetch((TimeFetchValue) value);
        }
        
        public abstract void timeFetch(TimeFetchValue value);
    }
}
