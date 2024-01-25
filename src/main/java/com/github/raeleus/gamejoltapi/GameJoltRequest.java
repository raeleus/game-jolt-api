package com.github.raeleus.gamejoltapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

/**
 * A request that may be submitted to the Game Jolt API through {@link GameJoltApi#sendRequest(GameJoltRequest, String,
 * GameJoltListener)} or {@link GameJoltApi#sendBatchRequest(Array, String, String, Boolean, Boolean,
 * GameJoltListener...)}.
 */
public interface GameJoltRequest {

    /**
     * Handles the server JSON response and returns a corresponding {@link GameJoltData}.
     * @param jsonValue The JSON response from the server.
     * @return The {@link GameJoltData} with the values returned from the server.
     */
    GameJoltData handleResponse(JsonValue jsonValue);
}
