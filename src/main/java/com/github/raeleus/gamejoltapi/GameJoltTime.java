package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.JsonValue;
import lombok.*;

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
            var builder = new StringBuilder();
            builder.append("/time/?game_id=").append(gameID);

            return builder.toString();
        }

        /**
         * Handles the server JSON response and returns a corresponding {@link TimeFetchData}.
         * @param jsonValue The JSON response from the server.
         * @return The {@link TimeFetchData} with the values returned from the server.
         */
        @Override
        public TimeFetchData handleResponse(JsonValue jsonValue) {
            return TimeFetchData.builder()
                .jsonValue(jsonValue)
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
    public static class TimeFetchData implements GameJoltData {

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
         * The hour of the day.
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
    }

    /**
     * Listener for {@link TimeFetchRequest}. Override {@link TimeFetchListener#timeFetch(TimeFetchData)} to
     * handle the server response.
     */
    public static abstract class TimeFetchListener extends GameJoltAdapter {
        @Override
        public void response(GameJoltData data) {
            if (data instanceof TimeFetchData) timeFetch((TimeFetchData) data);
        }

        public abstract void timeFetch(TimeFetchData data);
    }
}
