package com.indexer.mover.view

import android.view.View
import com.indexer.mover.R.string
import com.indexer.mover.base.BaseViewHolder
import com.indexer.mover.model.Job
import com.indexer.mover.rest.Config
import kotlinx.android.synthetic.main.job_item.view.accept
import kotlinx.android.synthetic.main.job_item.view.address
import kotlinx.android.synthetic.main.job_item.view.company_name
import kotlinx.android.synthetic.main.job_item.view.job_number

class UserJobViewHolder(itemView: View, listener: OnItemClickListener?) :
    BaseViewHolder(itemView, listener) {

  fun onBind(userJob: Job) {
    itemView.job_number.text = itemView.context.getString(string.jobnumber) +userJob.job_id
    itemView.company_name.text = itemView.context.getString(string.companyname) +userJob.company
    itemView.address.text = itemView.context.getString(string.address) +userJob.address

    itemView.accept.setOnClickListener {
      listener?.onItemClick(Config.accept)
    }
  }


}