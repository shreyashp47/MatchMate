package com.shreyash.matchmate.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shreyash.matchmate.databinding.ItemMatchCardBinding
import com.shreyash.matchmate.model.MatchStatus
import com.shreyash.matchmate.model.UserProfile

class MatchAdapter(
    private val onAction: (UserProfile, MatchStatus) -> Unit
) : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    private var profiles: List<UserProfile> = emptyList()

    fun submitList(list: List<UserProfile>) {
        profiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding =
            ItemMatchCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    override fun getItemCount() = profiles.size

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(profiles[position])
    }

    inner class MatchViewHolder(private val binding: ItemMatchCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: UserProfile) {
            binding.tvName.text = profile.name + ", " + profile.age
            binding.tvScore.text = "${profile.matchScore}% Match"
            binding.tvDescription.text = "${profile.education} | ${profile.religion} \nCity: ${profile.location}"
            Glide.with(binding.ivProfile.context).load(profile.imageUrl).into(binding.ivProfile)

            binding.btnAccept.isEnabled = profile.status == MatchStatus.NONE
            binding.btnDecline.isEnabled = profile.status == MatchStatus.NONE

            binding.btnAccept.setOnClickListener { onAction(profile, MatchStatus.ACCEPTED) }
            binding.btnDecline.setOnClickListener { onAction(profile, MatchStatus.DECLINED) }

            binding.tvStatus.text = when (profile.status) {
                MatchStatus.ACCEPTED -> {
                    binding.tvStatus.visibility = View.VISIBLE
                    binding.likeDislikeButtonsContainer.visibility = View.GONE
                    "Accepted"
                }

                MatchStatus.DECLINED -> {
                    binding.tvStatus.visibility = View.VISIBLE
                    binding.likeDislikeButtonsContainer.visibility = View.GONE
                    "Declined"
                }

                else -> {
                    binding.tvStatus.visibility = View.GONE
                    binding.likeDislikeButtonsContainer.visibility = View.VISIBLE
                    ""
                }
            }
        }
    }
}

