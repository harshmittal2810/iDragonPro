package com.idragonpro.andmagnus.fragments

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.idragonpro.andmagnus.R
import com.idragonpro.andmagnus.activities.DownloadedVideoInfo
import com.idragonpro.andmagnus.activities.viewModel.DownloadsViewModel
import com.idragonpro.andmagnus.activities.viewModel.VidInfoViewModel
import com.idragonpro.andmagnus.database.Download
import com.idragonpro.andmagnus.database.DownloadProgress
import com.idragonpro.andmagnus.helpers.SaveSharedPreference
import com.idragonpro.andmagnus.utils.CustomProgressBarDrawable
import com.idragonpro.andmagnus.utils.FileNameUtils
import com.idragonpro.andmagnus.utils.FileNameUtils.getMediaDuration
import com.idragonpro.andmagnus.utils.Utils
import com.idragonpro.andmagnus.utils.Utils.Companion.getDaysDifference
import com.idragonpro.andmagnus.utils.Utils.Companion.getStringSizeLengthFile
import com.idragonpro.andmagnus.work.CancelReceiver
import com.skydoves.progressview.ProgressView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class AllDownloadFragment : Fragment() {

    private lateinit var downloadsList: RecyclerView
    private lateinit var llBottom: LinearLayout
    private lateinit var rlTopSelected: RelativeLayout
    private lateinit var mainContent: RelativeLayout
    private lateinit var imgCancel: ImageView
    private lateinit var llDeleteSelected: LinearLayout
    private lateinit var llSelectAll: LinearLayout
    private var progressReceiver: BroadcastReceiver? = null
    private var deleteProgressReceiver: BroadcastReceiver? = null

    lateinit var downloadAdapter: DownloadAdapter
    lateinit var txtSelectedCount: TextView

    lateinit var progressView: ProgressView
    lateinit var tvStorageLabelPercent: TextView
    lateinit var tvBusySpace: TextView
    lateinit var tvTotalSpace: TextView
    lateinit var tvErrorInternetInstruction: TextView


    var downloadsViewModel: DownloadsViewModel? = null
    var downloadProgress: VidInfoViewModel? = null
    var selectedList: ArrayList<DownloadData> = ArrayList()
    var isSelectedMode: Boolean = false

    private var mView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        mView = inflater.inflate(R.layout.fragment_all_download, container, false)

        downloadsViewModel = ViewModelProvider(this)[DownloadsViewModel::class.java]
        downloadProgress = ViewModelProvider(this)[VidInfoViewModel::class.java]
        downloadsList = mView!!.findViewById(R.id.downloadsList)
        downloadAdapter = DownloadAdapter()
        downloadsList.layoutManager = LinearLayoutManager(activity)
        downloadsList.adapter = downloadAdapter
        llSelectAll = mView!!.findViewById(R.id.llSelectAll)
        llDeleteSelected = mView!!.findViewById(R.id.llDeleteSelected)
        imgCancel = mView!!.findViewById(R.id.imgCancel)
        txtSelectedCount = mView!!.findViewById(R.id.txtSelectedCount)
        rlTopSelected = mView!!.findViewById(R.id.rlTopSelected)
        mainContent = mView!!.findViewById(R.id.mainContent)
        llBottom = mView!!.findViewById(R.id.llBottom)

        progressView = mView!!.findViewById(R.id.progress_view)
        tvStorageLabelPercent = mView!!.findViewById(R.id.tv_storage_label)
        tvBusySpace = mView!!.findViewById(R.id.tv_busy_space)
        tvTotalSpace = mView!!.findViewById(R.id.textView4)
        tvErrorInternetInstruction = mView!!.findViewById(R.id.errorInternetInstructionText)

        imgCancel.setOnClickListener { unSelectAll() }

        llDeleteSelected.setOnClickListener {
            showDeleteDialog()
        }

        llSelectAll.setOnClickListener {
            selectedList.clear()
            selectedList.addAll(downloadAdapter.downloadList)
            txtSelectedCount.text = selectedList.size.toString() + " selected"
            downloadAdapter.notifyDataSetChanged()
        }

        progressReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.action == "DOWNLOAD_PROGRESS") {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        handleDownloadProgress(intent)
                    } else {
                        handleDownloadProgressIn(intent)
                    }
                }
            }
        }

        deleteProgressReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.action == "DELETE_PROGRESS") {
                    CoroutineScope(Dispatchers.IO).launch {
                        val downloadProgressList = downloadsViewModel?.allProgress?.value
                        if (!downloadProgressList.isNullOrEmpty()) {
                            val taskId = intent.getStringExtra("taskId")
                            val videoId = intent.getStringExtra("videoId")
                            val downloadInfo =
                                downloadProgressList.firstOrNull { it.taskId == taskId || it.videoId == videoId }
                            if (downloadInfo != null) {
                                downloadProgress!!.delete(downloadInfo)
                            }
                        }
                    }
                }
            }
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            progressReceiver as BroadcastReceiver, IntentFilter("DOWNLOAD_PROGRESS")
        )

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            deleteProgressReceiver as BroadcastReceiver, IntentFilter("DELETE_PROGRESS")
        )

        updateExternalStorage()

        return mView!!
    }

    private fun updateExternalStorage() {
        tvBusySpace.text = Utils.bytesToHuman(Utils.busyMemory())
        tvTotalSpace.text = " / ".plus(Utils.bytesToHuman(Utils.totalMemory()))
        val busySpacePercent = ((Utils.busyMemory().toFloat() / Utils.totalMemory()) * 100).toInt()
        tvStorageLabelPercent.text = "Used Space $busySpacePercent%"
        progressView.progress = busySpacePercent.toFloat()
    }

    private fun showDeleteDialog() {
        val dialog = Dialog((requireActivity()))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirmation)
        val txtTitle: TextView = dialog.findViewById(R.id.txtTitle)
        val txtDesc: TextView = dialog.findViewById(R.id.txtDesc)
        txtTitle.text = "Confirm"
        txtDesc.text = "Are you sure you want to delete this videos?"
        val txtNO: TextView = dialog.findViewById(R.id.txtNO)
        val txtOK: TextView = dialog.findViewById(R.id.txtOK)
        txtNO.setOnClickListener { dialog.dismiss() }
        txtOK.setOnClickListener {
            dialog.dismiss()
            for (download in selectedList) {
                val file = File(download.download!!.downloadedPath)
                file.takeIf { it.exists() }?.delete()
                downloadAdapter.downloadList.remove(download)
                updateExternalStorage()
            }
            selectedList.clear()
            downloadAdapter.notifyDataSetChanged()
            unSelectAll()
        }
        dialog.show()
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showCancelDialog(downloadInfo: DownloadProgress) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirmation)

        val txtTitle: TextView = dialog.findViewById(R.id.txtTitle)
        val txtDesc: TextView = dialog.findViewById(R.id.txtDesc)
        txtTitle.text = "Confirm"
        txtDesc.text = "Are you sure you want to cancel download this video?"
        val txtNO: TextView = dialog.findViewById(R.id.txtNO)
        val txtOK: TextView = dialog.findViewById(R.id.txtOK)
        txtNO.setOnClickListener { dialog.dismiss() }
        txtOK.setOnClickListener {
            val cancelIntent = Intent(context, CancelReceiver::class.java)
            cancelIntent.putExtra("taskId", downloadInfo.taskId)
            cancelIntent.putExtra("notificationId", downloadInfo.taskId.hashCode())
            activity?.sendBroadcast(cancelIntent)
            CoroutineScope(Dispatchers.IO).launch {
                downloadProgress!!.delete(downloadInfo)
            }
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun handleDownloadProgress(intent: Intent) {
        val downloadInfoArrayList: ArrayList<DownloadProgress>? =
            intent.getParcelableArrayListExtra("downloadList", DownloadProgress::class.java)
        downloadInfoArrayList?.forEach { progress ->
            CoroutineScope(Dispatchers.IO).launch {
                downloadProgress?.update(progress)
            }
        }
    }

    @Suppress("DEPRECATION")
    fun handleDownloadProgressIn(intent: Intent) {
        val downloadInfoArrayList: ArrayList<DownloadProgress>? =
            intent.getParcelableArrayListExtra("downloadList")
        downloadInfoArrayList?.forEach { progress ->
            CoroutineScope(Dispatchers.IO).launch {
                downloadProgress?.update(progress)
            }
        }
    }

    private fun unSelectAll() {
        txtSelectedCount.text = "0 selected"
        isSelectedMode = false
        selectedList.clear()
        downloadAdapter.notifyDataSetChanged()
//        (activity as AllDownloadActivity?)!!.navView.visibility = View.VISIBLE
        llBottom.visibility = View.GONE
        rlTopSelected.visibility = View.GONE
        mainContent.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver((progressReceiver)!!)
    }

    override fun onResume() {
        super.onResume()
        observeAllDownloadedList()
        observeAllDownloadProgress()
    }

    private fun observeAllDownloadedList() {
        lifecycleScope.launch(Dispatchers.Main) {
            downloadsViewModel!!.downloadedList.collect { downloads: List<Download>? ->
                val list = ArrayList(downloads!!)
                for (download in list) {
                    val file = File(download.downloadedPath)
                    if (file.exists()
                        && download.mobileNumber == SaveSharedPreference.getMobileNumber(
                            requireContext()
                        )
                    ) {
                        val days = getDaysDifference(Date().time, download.expiryDate)
                        if (days <= 0) {
                            downloadsViewModel!!.startDelete(download.id, requireContext())
                        } else {
                            downloadAdapter.addDownload(download)
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            downloadProgress!!.deleteWithId(download.videoId)
                            observeAllDownloadProgress()
                        }
                    }
                    downloadAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun observeAllDownloadProgress() {
        lifecycleScope.launch(Dispatchers.Main) {
            downloadsViewModel!!.allProgress.observe(viewLifecycleOwner) { list ->
                updateExternalStorage()
                val downloadList = list.filter {
                    it.mobileNumber == SaveSharedPreference.getMobileNumber(
                        requireContext()
                    )
                }
                if (downloadList.isNotEmpty()) {
                    tvErrorInternetInstruction.visibility = View.VISIBLE
                } else {
                    tvErrorInternetInstruction.visibility = View.GONE
                }

                downloadAdapter.loadProgress(downloadList)
            }
        }

    }


    inner class DownloadAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val downloadList: MutableList<DownloadData> = ArrayList()
        var downloaded: Boolean = false
        private var progressList: List<DownloadProgress>? = ArrayList()
        private var progressViewHolder: ProgressViewHolder? = null

        fun loadProgress(downloadDataList: List<DownloadProgress>) {
            progressList = downloadDataList
            notifyDataSetChanged()
        }

        fun addDownload(download: Download) {
            var found = false
            var data: DownloadData? = null
            var dataPosition: Int = -1

            for (i in downloadList.indices) {
                val downloadData: DownloadData = downloadList[i]
                if (downloadData.id.toLong() == download.id) {
                    data = downloadData
                    dataPosition = i
                    found = true
                    break
                }
            }

            if (!found) {
                val downloadData = DownloadData()
                downloadData.id = download.id.toInt()
                downloadData.download = download
                downloadList.add(downloadData)
                downloadList.sortWith { o1, o2 ->
                    o2.download!!.timestamp.toString().compareTo(o1.download!!.timestamp.toString())
                }
                notifyItemInserted(downloadList.indexOf(downloadData))
            } else {
                data!!.download = download
                notifyItemChanged(dataPosition)
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
        ): RecyclerView.ViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            return if (viewType == Companion.VIEW_TYPE_DOWNLOAD) {
                val view: View = inflater.inflate(R.layout.item_download, parent, false)
                ProgressViewHolder(view)
            } else {
                val view: View = inflater.inflate(R.layout.item_download, parent, false)
                ViewHolder(view)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (position < progressList!!.size) {
                Companion.VIEW_TYPE_DOWNLOAD
            } else {
                Companion.VIEW_TYPE_ITEM
            }
        }

        override fun onBindViewHolder(itemHolder: RecyclerView.ViewHolder, position: Int) {
            if (itemHolder is ViewHolder) {
                val holder: ViewHolder = itemHolder
                val downloadData: DownloadData = downloadList[position - progressList!!.size]

                Glide.with((activity)!!).load(downloadData.download!!.thumbnail)
                    .into(holder.imgVideo)

                holder.downloadVideoName.text = downloadData.download!!.name

                holder.imgVideo.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        if (isSelectedMode) {
                            var isContain = false
                            for (data: DownloadData in selectedList) {
                                if (data.download!!.id == downloadData.download!!.id) {
                                    isContain = true
                                }
                            }
                            if (isContain) {
                                selectedList.remove(downloadData)
                            } else {
                                selectedList.add(downloadData)
                            }
                            txtSelectedCount.text = selectedList.size.toString() + " selected"
                            notifyDataSetChanged()
                            return
                        }
                        if (downloaded && downloadData.download!!.mobileNumber.equals(
                                SaveSharedPreference.getMobileNumber(requireContext()),
                                true
                            )
                        ) {
                            playVideo(
                                downloadData.download!!.downloadedPath,
                                downloadData.download!!.ext,
                                downloadData.download!!.name,
                            )
                        }
                    }
                })

                holder.imgVideo.setOnLongClickListener {
                    if (!isSelectedMode) {
                        selectedList.add(downloadData)
                        isSelectedMode = true
                        txtSelectedCount.text = selectedList.size.toString() + " selected"
                        downloadAdapter.notifyDataSetChanged()
                        llBottom.visibility = View.VISIBLE
                        rlTopSelected.visibility = View.VISIBLE
                        mainContent.visibility = View.VISIBLE
                    }
                    false
                }

                holder.llContent.setOnLongClickListener {
                    if (!isSelectedMode) {
                        selectedList.add(downloadData)
                        isSelectedMode = true
                        txtSelectedCount.text = selectedList.size.toString() + " selected"
                        downloadAdapter.notifyDataSetChanged()
                        llBottom.visibility = View.VISIBLE
                        rlTopSelected.visibility = View.VISIBLE
                        mainContent.visibility = View.VISIBLE
                    }
                    false
                }

                holder.imgSelect.setOnClickListener {
                    var isContain = false
                    for (data: DownloadData in selectedList) {
                        if (data.download!!.id == downloadData.download!!.id) {
                            isContain = true
                        }
                    }
                    if (isContain) {
                        selectedList.remove(downloadData)
                    } else {
                        selectedList.add(downloadData)
                    }
                    txtSelectedCount.text = selectedList.size.toString() + " selected"
                    notifyDataSetChanged()
                }

                holder.llContent.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        if (isSelectedMode) {
                            var isContain = false
                            for (data: DownloadData in selectedList) {
                                if (data.download!!.id == downloadData.download!!.id) {
                                    isContain = true
                                }
                            }
                            if (isContain) {
                                selectedList.remove(downloadData)
                            } else {
                                selectedList.add(downloadData)
                            }
                            txtSelectedCount.text = selectedList.size.toString() + " selected"
                            notifyDataSetChanged()
                            return
                        }

                        if (downloaded && downloadData.download!!.mobileNumber.equals(
                                SaveSharedPreference.getMobileNumber(requireContext()),
                                true
                            )
                        ) {
                            playVideo(
                                downloadData.download!!.downloadedPath,
                                downloadData.download!!.ext,
                                downloadData.download!!.name,
                            )
                        }
                    }
                })

                holder.imgMore.setOnClickListener {
                    val popup = PopupMenu(activity, holder.imgMore)
                    popup.menuInflater.inflate(R.menu.menu_download, popup.menu)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {

                            R.id.menu_delete -> {
                                val dialog = Dialog((activity)!!)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setContentView(R.layout.dialog_confirmation)

                                val txtTitle: TextView = dialog.findViewById(R.id.txtTitle)
                                val txtDesc: TextView = dialog.findViewById(R.id.txtDesc)
                                txtTitle.text = "Confirm"
                                txtDesc.text = "Are you sure you want to delete this video?"
                                val txtNO: TextView = dialog.findViewById(R.id.txtNO)
                                val txtOK: TextView = dialog.findViewById(R.id.txtOK)
                                txtNO.setOnClickListener { dialog.dismiss() }
                                txtOK.setOnClickListener {
                                    val index =
                                        downloadList.indexOfFirst { it.id == downloadData.id }
                                    downloadsViewModel!!.startDelete(
                                        downloadList[index].id.toLong(), dialog.context
                                    )
                                    downloadList.removeAt(index)
                                    notifyItemRemoved(index)
                                    notifyItemChanged(index)
                                    dialog.dismiss()
                                    updateExternalStorage()
                                }
                                dialog.show()
                                dialog.window!!.setLayout(
                                    WindowManager.LayoutParams.MATCH_PARENT,
                                    WindowManager.LayoutParams.MATCH_PARENT
                                )
                                dialog.window!!.setBackgroundDrawable(
                                    ColorDrawable(
                                        Color.TRANSPARENT
                                    )
                                )
                            }
                        }
                        true
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        popup.setForceShowIcon(true)
                    }
                    popup.show()
                }

                holder.imgCancel.visibility = View.GONE
                holder.txtDuration.visibility = View.GONE
                holder.imgMore.visibility = View.GONE
                holder.imgSelect.visibility = View.GONE

                holder.downloadProgressBar.visibility = View.VISIBLE

                downloadsViewModel!!.loadState.observe(viewLifecycleOwner,
                    Observer({ state: WorkInfo.State ->
                        Log.d(TAG, "onBindViewHolder: $state")
                        when (state) {
                            WorkInfo.State.FAILED -> {
                                holder.imgCancel.visibility = View.VISIBLE
                                val strDescFailed: String =
                                    "Failed " + downloadData.download!!.downloadedPercent + "% " + getStringSizeLengthFile(
                                        downloadData.download!!.downloadedSize
                                    ) + "/" + getStringSizeLengthFile(
                                        downloadData.download!!.totalSize
                                    )
                                holder.downloadTextSize.text = strDescFailed
                            }

                            WorkInfo.State.RUNNING -> {}
                            WorkInfo.State.ENQUEUED -> {
                                holder.imgCancel.visibility = View.VISIBLE
                            }

                            WorkInfo.State.SUCCEEDED -> {
                                downloaded = true
                                holder.imgCancel.visibility = View.VISIBLE
                                holder.imgCancel.visibility = View.GONE
                                holder.downloadProgressBar.visibility = View.INVISIBLE
                                holder.downloadProgressBar1.visibility = View.INVISIBLE
                                holder.txtDuration.visibility = View.VISIBLE
                                holder.imgMore.visibility = View.VISIBLE
                                val dateString: String = SimpleDateFormat("MMMM dd yyyy").format(
                                    Date(
                                        downloadData.download!!.timestamp
                                    )
                                )

                                val duration = if (downloadData.download!!.duration <= 0) {
                                    try {
                                        File(downloadData.download!!.downloadedPath).getMediaDuration(
                                            holder.txtDuration.context
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        downloadData.download!!.duration
                                    }
                                } else {
                                    downloadData.download!!.duration
                                }

                                val fileSize = if (downloadData.download!!.downloadedSize <= 0) {
                                    FileNameUtils.getFileSize(downloadData.download!!.downloadedPath)
                                } else {
                                    downloadData.download!!.downloadedSize
                                }

                                val strDescComplete: String =
                                    getStringSizeLengthFile(fileSize) + "  " + dateString

                                holder.downloadTextSize.text = strDescComplete
                                holder.txtDuration.text = Utils.formatDuration(duration)
                            }

                            WorkInfo.State.CANCELLED -> {
                                val strDesc2: String =
                                    "Cancelled " + downloadData.download!!.downloadedPercent + "% " + getStringSizeLengthFile(
                                        downloadData.download!!.downloadedSize
                                    ) + "/" + getStringSizeLengthFile(
                                        downloadData.download!!.totalSize
                                    )
                                holder.downloadTextSize.text = strDesc2
                                holder.imgCancel.visibility = View.GONE
                            }

                            WorkInfo.State.BLOCKED -> {}
                            else -> {}
                        }
                    } as (WorkInfo.State?) -> Unit))
                if (isSelectedMode) {
                    holder.imgCancel.visibility = View.GONE
                    holder.imgMore.visibility = View.GONE
                    holder.imgSelect.visibility = View.VISIBLE
                    var isContain = false
                    for (data: DownloadData in selectedList) {
                        if (data.download!!.id == downloadData.download!!.id) {
                            isContain = true
                        }
                    }
                    if (isContain) {
                        Glide.with((activity)!!).load(R.drawable.ic_box_selected)
                            .into(holder.imgSelect)
                    } else {
                        Glide.with((activity)!!).load(R.drawable.ic_box_unselect)
                            .into(holder.imgSelect)
                    }
                }
            } else if (itemHolder is ProgressViewHolder) {
                progressViewHolder = itemHolder

                val downloadInfo: DownloadProgress = progressList!![position]

                if (downloadInfo.line.contains("download waiting.....")) {
                    progressViewHolder!!.downloadProgressBar1.progressDrawable =
                        CustomProgressBarDrawable(requireContext().getColor(R.color.primary_green))
                    progressViewHolder!!.downloadProgressBar1.visibility = View.VISIBLE
                    progressViewHolder!!.downloadProgressBar.visibility = View.GONE
                } else {
                    progressViewHolder!!.downloadProgressBar1.visibility = View.GONE
                    progressViewHolder!!.downloadProgressBar.visibility = View.VISIBLE
                    progressViewHolder!!.downloadProgressBar.progress = downloadInfo.progress
                }

                if (downloadInfo.progress == 100) {
                    progressViewHolder!!.downloadProgressBar1.visibility = View.GONE
                    progressViewHolder!!.downloadProgressBar.visibility = View.VISIBLE
                    progressViewHolder!!.downloadProgressBar.isIndeterminate = true

                    val text = "Processing video..."
                    val builder = SpannableStringBuilder(text)
                    val start = 0
                    val end = text.length
                    builder.setSpan(
                        StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.setSpan(
                        ForegroundColorSpan(Color.parseColor("#00a69c")),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    progressViewHolder!!.downloadSize.visibility = View.GONE
                    progressViewHolder!!.imgCancel.visibility = View.GONE
                    progressViewHolder!!.downloadSpeed.text = builder
                } else {
                    val result = Utils.extractPercentageAndMB(downloadInfo.line)
                    if (result != null) {
                        val (percentage, sizeInMB, speedInKiB) = result
                        val mb = String.format("%.2f MB/S", speedInKiB / 1024)
                        val text = "$mb+${speedInKiB}KB/S"
                        val builder = SpannableStringBuilder(text)
                        val start = 0
                        val end = mb.length
                        builder.setSpan(
                            StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        builder.setSpan(
                            ForegroundColorSpan(Color.parseColor("#00a69c")),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        progressViewHolder!!.downloadSize.text =
                            "$percentage %/$sizeInMB " + if (downloadInfo.line.contains(
                                    "GiB", true
                                )
                            ) "GB" else "MB"
                        progressViewHolder!!.downloadSpeed.text = builder
                    } else {
                        progressViewHolder!!.downloadSize.text = "0.00%/0.00MB"
                        Log.d(TAG, "onBindViewHolder: ${downloadInfo.size}")
                        progressViewHolder!!.downloadSpeed.text = "0.00KB/S+0.00MB/S"
                    }
                }

                progressViewHolder!!.imgSelect.visibility = View.GONE
                progressViewHolder!!.imgMore.visibility = View.GONE
                progressViewHolder!!.downloadVideoName.text = downloadInfo.name
                Glide.with(requireContext()).load(downloadInfo.thumbnail)
                    .into(progressViewHolder!!.imgVideo)

                progressViewHolder!!.imgCancel.setOnClickListener {
                    showCancelDialog(downloadInfo)
                }

                /*if (downloadInfo.progress == 100) {
                    CoroutineScope(Dispatchers.IO).launch {
                        downloadProgress!!.delete(downloadInfo)
                    }
                }*/
            }
        }

        override fun getItemCount(): Int {
            return downloadList.size + progressList!!.size
        }

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var downloadVideoName: TextView = itemView.findViewById(R.id.downloadVideoName)
            var downloadProgressBar: ProgressBar = itemView.findViewById(R.id.downloadProgressBar)
            var downloadProgressBar1: ProgressBar = itemView.findViewById(R.id.downloadProgressBar1)
            var imgVideo: ImageView = itemView.findViewById(R.id.imgVideo)
            var downloadTextSpeed: TextView = itemView.findViewById(R.id.download_speed)
            var downloadTextSize: TextView = itemView.findViewById(R.id.download_size)
            var imgCancel: ImageView = itemView.findViewById(R.id.imgCancel)
            var imgMore: ImageView = itemView.findViewById(R.id.imgMore)
            var txtDuration: TextView = itemView.findViewById(R.id.txtDuration)
            var llContent: LinearLayout = itemView.findViewById(R.id.llContent)
            var imgSelect: ImageView = itemView.findViewById(R.id.imgSelect)
        }

        inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var downloadProgressBar: ProgressBar = itemView.findViewById(R.id.downloadProgressBar)
            var downloadProgressBar1: ProgressBar = itemView.findViewById(R.id.downloadProgressBar1)
            var imgVideo: ImageView = itemView.findViewById(R.id.imgVideo)
            var downloadSize: TextView = itemView.findViewById(R.id.download_size)
            var downloadSpeed: TextView = itemView.findViewById(R.id.download_speed)
            var downloadVideoName: TextView = itemView.findViewById(R.id.downloadVideoName)
            var imgMore: ImageView = itemView.findViewById(R.id.imgMore)
            var imgCancel: ImageView = itemView.findViewById(R.id.imgCancel)
            var imgSelect: ImageView = itemView.findViewById(R.id.imgSelect)
        }
    }

    private fun playVideo(downloadedPath: String, ext: String, name: String) {
        DownloadedVideoInfo.playDownloadVideo(
            requireActivity(),
            videoURL = downloadedPath,
            ext = ext,
            videoTitle = name
        )

        /*lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val filePath = FileNameUtils.decryptFile(
                    downloadedPath,
                    FileNameUtils.generateSecretKey(),
                    requireContext()
                )
                DownloadedVideoInfo.playDownloadVideo(requireActivity(), filePath)
            }
        }*/
    }

    class DownloadData {
        var id: Int = 0
        var download: Download? = null
        var eta: Long = -1
        var downloadedBytesPerSecond: Long = 0
        override fun hashCode(): Int {
            return id
        }

        override fun toString(): String {
            if (download == null) {
                return ""
            }
            return download.toString()
        }

        override fun equals(obj: Any?): Boolean {
            return obj === this || obj is DownloadData && obj.id == id
        }
    }

    companion object {
        private val TAG: String = AllDownloadFragment::class.java.simpleName
        const val VIEW_TYPE_DOWNLOAD: Int = 0
        const val VIEW_TYPE_ITEM: Int = 1
    }
}