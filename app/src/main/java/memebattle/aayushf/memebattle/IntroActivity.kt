package memebattle.aayushf.memebattle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import org.jetbrains.anko.startActivity

class IntroActivity : IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(SimpleSlide.Builder().title("Welcome To MemeBattle").description("It's A Beta Version").background(R.color.cardBackGround).image(R.mipmap.ic_launcher).backgroundDark(R.color.windowBackground).scrollable(false).build())
        addSlide(SimpleSlide.Builder().title("So, How Do I Play This Game?").description("Swipe Left To Continue").background(R.color.cardBackGround).image(R.mipmap.ic_launcher).backgroundDark(R.color.windowBackground).scrollable(false).build())
        addSlide(SimpleSlide.Builder().title("Step 1: Starting/Joining A Game").description("Start A Game By Pressing The \"Add\"Button, or Join An Existing Game By Entering Its Game Code.").background(R.color.cardBackGround).image(R.mipmap.ic_launcher).backgroundDark(R.color.windowBackground).scrollable(false).build())
        addSlide(SimpleSlide.Builder().title("Step 2: Uploading Images").description("Upload Images For Making The Memes, These Can Be Images Of Your Friends, Or Anything Else, Which Can Be Made Into A Meme.").background(R.color.cardBackGround).image(R.mipmap.ic_launcher).backgroundDark(R.color.windowBackground).scrollable(false).build())
        addSlide(SimpleSlide.Builder().title("Step 3: Create Captions!").description("Now, Make Up Witty Captions For The Image That Appears On Your Screen, And Then Press The Tick Button To Submit It").background(R.color.cardBackGround).image(R.mipmap.ic_launcher).backgroundDark(R.color.windowBackground).scrollable(false).build())
        addSlide(SimpleSlide.Builder().title("Step 4: Choose!").description("Select The Meme You Think Is The Funniest, Remember, You Cannot Select The One You Made Yourself.\nEach Person Gets 5 Points Everytime Their Captions Are Selected, Earn The Maximum Points To Win The Game!").background(R.color.cardBackGround).image(R.mipmap.ic_launcher).backgroundDark(R.color.windowBackground).scrollable(false).build())
    }
}
