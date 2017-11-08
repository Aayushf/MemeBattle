package memebattle.aayushf.memebattle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import co.zsmb.materialdrawerkt.draweritems.switchable.switchItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.design.textInputLayout
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

class MainActivity : AppCompatActivity() {


    operator fun DatabaseReference.get(ref: String): DatabaseReference {
        return this.child(ref)

    }

    fun View.disappear() {
        val animator = android.animation.AnimatorInflater.loadAnimator(this@MainActivity, R.animator.alpha_animator_out)
        animator.setTarget(this)
        animator.start()
        this.visibility = View.INVISIBLE


    }

    fun View.appear() {
        val animator = android.animation.AnimatorInflater.loadAnimator(this@MainActivity, R.animator.alpha_animator_in)
        animator.setTarget(this)
        this.visibility = View.VISIBLE
        animator.start()

    }
//



    private fun allgone() {
        join_game_card.disappear()
        card_upload_images.disappear()
        create_meme_card.disappear()
        title_text.disappear()
        subtitle_text.disappear()
        main_image.disappear()
        meme_top_caption_text.disappear()
        meme_bottom_caption_text.disappear()
        rv_main.disappear()
        fab_share.disappear()
        below_subtitle_text.disappear()
        dimmer.disappear()
    }

    operator fun StorageReference.get(ref: String): StorageReference {
        return this.child(ref)

    }

    operator fun DatabaseReference.plus(run: (p0: DataSnapshot) -> Unit) {
        this.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                debug("Error Contacting Database")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 == null || p0.value == null) {
                    debug("Null DataSnapshot")
                } else {
                    run(p0)
                }

            }

        })
    }

    operator fun DatabaseReference.minus(run: (p0: DataSnapshot) -> Unit) {
        this.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                debug("Cancelled")
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                if (p0 != null) {
                    run(p0!!)
                } else {
                    debug("Null DataSnapshot")
                }

            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })
    }

    operator fun DatabaseReference.div(run: (p0: DataSnapshot) -> Unit) {
        this.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                debug("Cancelled")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    run(p0)
                } else {
                    debug("Null DataSnapshot")
                }
            }

        })
    }

    operator fun StorageReference.minus(run: (uri: Uri) -> Unit) {
        this.downloadUrl.addOnSuccessListener {
            run(it)
            debug("Got Download Url")

        }.addOnFailureListener {
            debug("Download Image Failed")
            it.printStackTrace()
        }

    }


    operator fun StorageReference.plus(p: Pair<Uri, String>): UploadTask {
        debug("Uploading File to $gameid")
        return this.child("${p.second}.jpg").putFile(p.first)

    }

    operator fun UploadTask.plus(run: () -> Unit) {
        this.addOnSuccessListener {
            debug("Upload Success")
            run()
        }
        this.addOnFailureListener {
            debug("Upload Failed")
        }
    }

    operator fun DatabaseReference.unaryPlus() {
        this.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                debug("Cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                allRounds = p0.children.map { it.key }.toMutableList()
            }

        })

    }

    //    set(value){
//        if(value != null){
//            //Aesthetic.get().colorWindowBackground(value.getDarkVibrantColor(resources.getColor(R.color.design_fab_shadow_end_color))).colorPrimary(value.getLightMutedColor(resources.getColor(R.color.colorPrimary))).colorAccent(value.getLightVibrantColor(resources.getColor(R.color.colorAccent))).textColorSecondary(value.getLightVibrantColor(resources.getColor(R.color.colorPrimary))).apply()
//        }
//    }
    fun allBottomGone() {
        card_upload_images.disappear()
        create_meme_card.disappear()
        join_game_card.disappear()
        main_pager.disappear()
        rv_main.disappear()
        dimmer.disappear()
    }

    val cloud = FirebaseStorage.getInstance().reference
    var allRounds = mutableListOf<String>()
        set(value) {
            field = value
            debug("allrounds set")
            startRound()

        }
    val fbd = FirebaseDatabase.getInstance()
    var name = "-1"
    var host = true

    var gameref = fbd.getReference("-1")
        set(value) {
            field = value
            curroundref = value["curround"]
            debug("gameref set")
            playersref = value.child("players")
            roundsref = value["rounds"]
        }
    var allPlayers = listOf<Pair<String, Int>>()
        set(value) {
            field = value
            setupDrawer(value.sortedBy {
                it.second
            }.reversed())
        }
    var selected = false
    var selectedmeme = Pair("Name", Pair("Top Caption", "Bottom Caption"))
        set(value) {
            field = value
            meme_top_caption_text.text = value.second.first
            meme_bottom_caption_text.text = value.second.second
            start_game_fab.disappear()
        }

    fun selectMeme(submitter: String) {
        val myselectionref = curselectionsref[name]
        myselectionref.setValue(submitter)
        selected = true
    }

    var curround = "-1"
        set(value) {
            field = value
            if (value == "e") {
                allBottomGone()
                rv_main.appear()
                rv_main.adapter = GameResultAdapter()
                start_game_fab.appear()
                start_game_fab.onClick {

                    startActivity<MainActivity>()
                }


            } else {
                debug("curround set as $value")
                gamecloud.child("${value}.jpg") - {
                    curimageUri = it
                }
                responded = false
                curresponsesref = gameref["rounds"][value]["responses"]
                curselectionsref = gameref["rounds"][value]["selections"]
                selected = false
            }
        }

    var curimageUri: Uri = Uri.EMPTY
        set(value) {
            field = value
            allgone()
            main_image.appear()
            meme_top_caption_text.appear()
            meme_bottom_caption_text.appear()
            create_meme_card.appear()
            bottom_caption_et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    meme_bottom_caption_text.text = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    meme_bottom_caption_text.text = s
                }

            })
            top_caption_et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    meme_top_caption_text.text = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    meme_top_caption_text.text = s
                }

            })
            Glide.with(this).load(value).asBitmap().into(object : SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    runOnUiThread {
                        main_image.setImageBitmap(resource!!)
                    }

                }

            })
            start_game_fab.setImageDrawable(IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_check).color(Color.BLUE).sizeDp(24))
            start_game_fab.onClick {
                Log.d("MainActivity", "FAB ONCLICK INVOKED")
                if (!responded) {
                    val myresponse = curresponsesref[name]
                    myresponse["top"].setValue(top_caption_et.text.toString())
                    myresponse["bottom"].setValue(bottom_caption_et.text.toString())
                    responded = true
                } else {
                    snackbar(start_game_fab, "You have already submitted your meme")
                }
            }


        }
    var responded = false
    var curroundref = gameref["curround"]
        set(value) {
            field = value
            debug("curroundref set")
            value + {
                debug("curround set as ${it.getValue(String::class.java)}")
                curround = it.getValue(String::class.java)
            }


        }
    var curresponsesref = gameref[curround]["responses"]
        set(value) {
            field = value
            debug("currresponseref set")
            value + { p0: DataSnapshot ->
                if (p0.childrenCount.toInt() == allPlayers.size) {
                    allResponses = p0.children.map { ds: DataSnapshot ->
                        Log.d("Response", "Response: Player:${ds.key}, Bottom:${ds.child("bottom").getValue(String::class.java)}, Top:${ds.child("top").getValue(String::class.java)}")
                        Pair(ds.key, Pair(ds.child("top").getValue(String::class.java), ds.child("bottom").getValue(String::class.java)))
                    }

                }

            }
        }
    var allResponses = listOf<Pair<String, Pair<String, String>>>()
        set(value) {
            field = value
            create_meme_card.disappear()
            debug("allResponses set")
            rv_main.appear()
            rv_main.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rv_main.adapter = Adapter(value)

//              main_pager.appear()
//              main_pager.adapter = Pager(value)

        }
    var roundsref = gameref["rounds"]
        set(value) {
            field = value
            value - {
                snackbar(card_upload_images, "A Round Was Added")

            }
        }

    var playersref = gameref.child("players")
        set(value) {
            field = value
            playerref = value.child(name)
            value + { p0: DataSnapshot ->
                allPlayers = p0.children.map { Pair(it.key, it.getValue(Long::class.java).toInt()) }
            }
            debug("playersref set")

        }
    var playerref = playersref.child("myplayer")
        set(value) {
            field = value
            value.setValue(0)
            debug("playerref set")
            debug("gameid = $gameid")

        }
    var gameid = "-1"
        set(value) {
            field = value
            allgone()
            gameref = fbd.getReference(value)
            title_text.appear()
            title_text.text = value
            subtitle_text.appear()
            subtitle_text.text = "Game Code"
            fab_share.appear()
            fab_share.onClick {
                val i = Intent(android.content.Intent.ACTION_SEND)
                i.setType("text/plain")
                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Memebattle Game Code")
                i.putExtra(android.content.Intent.EXTRA_TEXT, "MemeBattle Game Code :$value")
                startActivity(Intent.createChooser(i, "Share Via"))


            }
            gamecloud = cloud[value]
            card_upload_images.appear()
            debug("gameid set as $value")
            if (host) {
                start_game_fab.onClick {
                    +roundsref
                }
                start_game_fab.setImageDrawable(IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_chevron_double_right).color(Color.BLUE).sizeDp(24))
            } else {
                start_game_fab.disappear()
            }

        }
    var gamecloud = cloud[gameid]
        set(value) {
            field = value
            debug("gamecloud set")
        }
    var curselectionsref = gameref[curround]["selections"]
        set(value) {
            field = value
            debug("curselectionsref set")
            value + {
                if (it.childrenCount.toInt() == allPlayers.size) {
                    debug("Everyone has responded")
                    val listOfResults = arrayListOf<Pair<Pair<String, Int>, Pair<String, String>>>()
                    for (i in 0 until allResponses.size) {
                        val name = allResponses[i].first
                        var number = 0
                        it.children.forEach { p0 ->
                            if (p0.getValue(String::class.java) == name) {
                                number++
                            }
                        }
                        listOfResults.add(Pair(Pair(name, number), allResponses[i].second))
                        if (host) {
                            playersref[name] / { p0: DataSnapshot ->
                                playersref[name].setValue(p0.getValue(Long::class.java).toInt() + number * 5)
                                debug("Score of $name updated")
                            }
                        }


                    }
                    rv_main.adapter = ResultsAdapter(listOfResults)
                    start_game_fab.appear()
                    start_game_fab.setImageDrawable(IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_chevron_double_right).color(Color.BLUE).sizeDp(24))
                    start_game_fab.onClick {
                        startRound()
                    }
                    title_text.appear()
                    val roundwinner = listOfResults.maxBy { p ->
                        p.first.second
                    }?.first?.first
                    dimmer.appear()
                    title_text.text = "$roundwinner Wins This Round!"


                } else {
                    debug("Someone selected")
                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        name = prefs.getString("NAME_PREF", "-1")
        if (name == "-1") {
            startActivity<IntroActivity>()
            alert {
                customView {
                    var e: EditText? = null
                    title = "Tell Us Your Name"
                    textInputLayout {
                        e = editText {

                        }
                    }
                    positiveButton("Done", {
                        prefs.edit().putString("NAME_PREF", e!!.text.toString()).commit()
                        name = e!!.text.toString()
                    })
                }
            }.show()
        }
        setContentView(R.layout.activity_main)
        fab_upload_image.onClick {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setFixAspectRatio(true).start(this@MainActivity)
            debug("Image Upload Clicked")
        }
        start_game_fab.bringToFront()
        createInitDrawer()
        val screenwidth = windowManager.defaultDisplay.width
        val lp = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenwidth)
        upperll.layoutParams = lp
        start_game_fab.onClick {
            gameid = UUID.randomUUID().toString().substring(0, 7)
            host = true
        }
        allgone()
        title_text.appear()
        subtitle_text.appear()
        join_game_card.appear()
        below_subtitle_text.appear()


        //Aesthetic.get().colorPrimaryRes(R.color.colorPrimary).colorAccentRes(R.color.colorAccent).isDark(true).textColorPrimaryRes(R.color.md_white_1000).textColorPrimaryInverseRes(R.color.md_white_1000).textColorSecondaryRes(R.color.md_white_1000).textColorSecondaryInverseRes(R.color.md_white_1000).apply()


        join_game_fab.onClick {
            gameid = code_et.text.toString()
            host = false

        }


    }


    fun setupDrawer(listOfPlayers: List<Pair<String, Int>>) {
        val score = listOfPlayers.find {
            it.first == name
        }!!.second
        val d = drawer {
            accountHeader {
                profile(name, score.toString())
            }

        }
        var firstIteration = true
        listOfPlayers.forEach {
            if (firstIteration) {
                firstIteration = false
                d.addItem(PrimaryDrawerItem().withName(it.first).withBadge(it.second.toString()).withIcon(CommunityMaterial.Icon.cmd_crown))
            } else {
                d.addItem(PrimaryDrawerItem().withName(it.first).withBadge(it.second.toString()).withIcon(CommunityMaterial.Icon.cmd_account_circle))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultURI = result.uri
                addRound(resultURI)
            }
        }
    }

    fun addRound(resultUri: Uri) {
        val roundID = UUID.randomUUID().toString().substring(0, 5)
        gamecloud + (Pair(resultUri, roundID)) + {
            debug("Adding Round To Database")
            roundsref[roundID]["uploader"].setValue(name)
        }

    }

    fun startRound() {
        debug("Inside startRound()")
        if (allRounds.size != 0) {
            val randomInt = Random().nextInt(allRounds.size)
            curroundref.setValue(allRounds[randomInt])
            allRounds.removeAt(randomInt)
        } else {
            snackbar(start_game_fab, "No More Rounds In Database, Game Complete")
            if (host) {
                curroundref.setValue("e")
            }

        }
    }

    inner class Adapter(val list: List<Pair<String, Pair<String, String>>>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            if (holder != null) {
                if (holder.toptext != null) {
                    holder.toptext.text = list[position].second.first
                }

                Log.d("Inside bindview", "toptext:${list[position].second.second}]")
            } else {
                Log.d("Inside bindview", "toptext is null")
            }
            if (holder != null) {
                if (holder.bottomtext != null) {
                    holder.bottomtext.text = list[position].second.second
                }

                holder.card.onClick {
                    selectedmeme = list[position]
                    holder?.fabSelect.appear()
                    holder?.fabSelect.setImageDrawable(IconicsDrawable(this@MainActivity).icon(CommunityMaterial.Icon.cmd_heart_outline).color(Color.BLUE).sizeDp(24))
                }
            }
            holder?.fabSelect?.disappear()
            holder?.fabSelect?.onClick {
                if (selected)
                    snackbar(start_game_fab, "You Have Already Selected Your Meme")
                else
                    selectMeme(list[position].first)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.response_item, parent, false))

        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class ViewHolder(val myItemView: View) : RecyclerView.ViewHolder(myItemView) {
            val card = myItemView.find<CardView>(R.id.response_item_card)
            val toptext = myItemView.find<TextView>(R.id.item_top)
            val bottomtext = myItemView.find<TextView>(R.id.item_bottom)
            val fabSelect = myItemView.find<FloatingActionButton>(R.id.select_fab_item)
        }
    }

    inner class ResultsAdapter(val list: List<Pair<Pair<String, Int>, Pair<String, String>>>) : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            if (holder != null) {
                holder.toptext.text = list[position].second.first
                holder.bottomtext.text = list[position].second.second
                holder.name.text = list[position].first.first
                holder.selections.text = list[position].first.second.toString()
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.result_item, parent, false))
        }

        inner class ViewHolder(private val myItemView: View) : RecyclerView.ViewHolder(myItemView) {
            val card = myItemView.find<CardView>(R.id.response_item_card)
            val toptext = myItemView.find<TextView>(R.id.textAll)
            val bottomtext = myItemView.find<TextView>(R.id.item_bottom)
            val name = myItemView.find<TextView>(R.id.name_result_item)
            val selections = myItemView.find<TextView>(R.id.selections_result_item)


        }

    }

    inner class Pager(val list: List<Pair<String, Pair<String, String>>>) : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            selectedmeme = list[position]
            Log.d("From getItem", "Bottom Caption = ${list[position].second.second}")
            return ResponseFragment(list[position])

        }

        override fun getCount(): Int {
            return list.size
        }


    }

    class ResponseFragment(val p: Pair<String, Pair<String, String>> = Pair("Name", Pair("Top", "Bottom"))) : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            return inflater.inflate(R.layout.response_item, container, false)

        }

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            if (view != null) {
                if (p.second.second != null) {
                    view.find<TextView>(R.id.textAll).text = p.second.second
                }
                view.find<TextView>(R.id.item_bottom).text = p.second.first
                Log.d("Fragment", "Bottom Caption = ${p.second.second}")

            }

        }


    }

    inner class GameResultAdapter : RecyclerView.Adapter<GameResultAdapter.ViewHolder>() {
        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.nametv?.text = allPlayers[position].first
            holder?.pointstv?.text = allPlayers[position].second.toString()
        }

        override fun getItemCount(): Int {
            return allPlayers.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.player_card, parent, false))


        }


        inner class ViewHolder(myItemView: View) : RecyclerView.ViewHolder(myItemView) {
            val nametv = myItemView.find<TextView>(R.id.player_name)
            val pointstv = myItemView.find<TextView>(R.id.player_points)
        }
    }

    fun createInitDrawer() {
        drawer {
            accountHeader {
                profile(name, "Master Meme Maker") {

                }
            }
            primaryItem {
                name = "About This App"
                onClick { v ->
                    startActivity<IntroActivity>()
                    false

                }
            }.withIcon(CommunityMaterial.Icon.cmd_information_outline)
            primaryItem {
                name = "Edit Name"
                onClick { _ ->
                    alert {
                        customView {
                            var e: EditText? = null
                            title = "Tell Us Your Name"
                            textInputLayout {
                                e = editText {

                                }
                            }
                            positiveButton("Done", {
                                val prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                                prefs.edit().putString("NAME_PREF", e!!.text.toString()).commit()
                                name = e!!.text.toString()
                            })
                        }
                    }.show()
                    false
                }
            }.withIcon(CommunityMaterial.Icon.cmd_account_edit)
            primaryItem {
                name = "Share"

                onClick { v ->
                    false
                }
            }.withIcon(CommunityMaterial.Icon.cmd_share_variant)
            primaryItem {
                name = "Send Feedback"
                onClick { _ ->
                    alert {
                        customView {
                            title = "Enter Your Feedback Below"
                            var e: EditText? = null
                            textInputLayout {
                                e = editText {

                                }
                            }
                            positiveButton("Send Feedback") {
                                email("aayushf@gmail.com", "MemeBattle Beta Feedback", "${e!!.text.toString()}")

                            }
                        }
                    }.show()
                    false
                }
            }.withIcon(CommunityMaterial.Icon.cmd_lightbulb_on_outline)
            switchItem {
                name = "Debug?"
                onSwitchChanged { drawerItem, button, isEnabled ->
                    val prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("DEBUG", isEnabled)


                }

            }.withIcon(CommunityMaterial.Icon.cmd_android_debug_bridge)
        }
    }

    fun debug(message: String) {
        if (getSharedPreferences("PREFS", Context.MODE_PRIVATE).getBoolean("DEBUG", false)) {
            toast(message)
        }
    }

}
