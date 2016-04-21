package com.github.willjgriff.playground.network.endpoints;

import com.github.willjgriff.playground.network.model.ethereum.Block;
import com.github.willjgriff.playground.network.model.ethereum.BlockCount;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Will on 18/04/2016.
 */
public interface ApiEtherchain {

    @GET("api/blocks/count")
    Call<BlockCount> totalBlockCount();

    @GET("api/blocks/{offset}/{count}")
    Call<List<Block>> blocks(@Path("offset") int offset, @Path("count") int count);
}