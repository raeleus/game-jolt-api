package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.JsonValue;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * A namespace to obtain time information from the Game Jolt server. It can be used to realize real-time gameplay for
 * everyone playing the same game or security systems based on the server time.
 */
public class GameJoltTime {
    
    /**
     * Returns the time of the Game Jolt server.
     */
    @Builder
    @Getter
    @Setter
    public static class TimeFetchRequest implements GameJoltRequest {
        
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
    }
    
    /**
     * The result of fetching the time from the server.
     */
    @Builder
    @Getter
    @Setter
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
        
        private String formatMinute(int minute) {
            return (minute >= 10 ? "" : "0") + minute;
        }
        
        @Override
        public String toString() {
            var standardHour = hour == 0 || hour == 12 ? 12 : hour % 12;
            return month + "/" + day + "/" + year + " " + standardHour + ":" + formatMinute(
                    minute) + ":" + formatMinute(second) + (hour < 12 ? " AM" : " PM");
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
