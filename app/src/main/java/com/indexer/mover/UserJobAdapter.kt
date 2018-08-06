package com.indexer.mover

import android.view.ViewGroup


import android.view.LayoutInflater
import com.indexer.mover.base.BaseAdapter
import com.indexer.mover.base.BaseViewHolder
import com.indexer.mover.model.Job
import com.indexer.mover.view.UserJobViewHolder

class UserJobAdapter (var onItemClickListener: BaseViewHolder.OnItemClickListener):
    BaseAdapter<UserJobViewHolder, Job>() {

  override fun onBindViewHolder(holder: UserJobViewHolder, position: Int) {
    holder.onBind(mItems[position],position)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserJobViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.job_item, parent, false)
    return UserJobViewHolder(view,onItemClickListener)
  }


}