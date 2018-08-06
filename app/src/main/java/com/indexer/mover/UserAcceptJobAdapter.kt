package com.indexer.mover

import android.view.ViewGroup

import android.view.LayoutInflater
import com.indexer.mover.base.BaseAdapter
import com.indexer.mover.base.BaseViewHolder
import com.indexer.mover.model.Job
import com.indexer.mover.model.JobAccept
import com.indexer.mover.view.UserJobAcceptViewHolder
import com.indexer.mover.view.UserJobViewHolder

class UserAcceptJobAdapter(var onItemClickListener: BaseViewHolder.OnItemClickListener) :
    BaseAdapter<UserJobAcceptViewHolder, JobAccept>() {

  override fun onBindViewHolder(
    holder: UserJobAcceptViewHolder,
    position: Int
  ) {
    holder.onBind(mItems[position], position)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): UserJobAcceptViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.job_item, parent, false)
    return UserJobAcceptViewHolder(view, onItemClickListener)
  }

}