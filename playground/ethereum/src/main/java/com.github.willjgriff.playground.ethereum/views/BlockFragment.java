package com.github.willjgriff.playground.ethereum.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.will.Playground.R;
import com.github.willjgriff.playground.ethereum.presenters.BlockPresenter;
import com.github.willjgriff.playground.ethereum.presenters.BlockPresenterImpl;
import com.github.willjgriff.playground.mvp.Remind101ExampleAdapted.View.MvpFragment;

/**
 * Created by Will on 18/04/2016.
 */
public class BlockFragment extends MvpFragment<BlockPresenter> implements BlockView {

    TextView mRxOutput;
    TextView mTotalBlockCount;
    TextView mBlockHash;

    @Override
    protected BlockPresenter createPresenter() {
        return new BlockPresenterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block, container, false);
        mTotalBlockCount = (TextView) view.findViewById(R.id.fragment_block_total_block_count);
        mBlockHash = (TextView) view.findViewById(R.id.fragment_block_current_hash);
        mRxOutput = (TextView) view.findViewById(R.id.fragment_block_rx_output);
        return view;
    }

    @Override
    public void setBlockCount(long count) {
        mTotalBlockCount.setText(String.valueOf(count));
    }

    @Override
    public void setBlockHash(String hash) {
        mBlockHash.setText(hash);
    }


}