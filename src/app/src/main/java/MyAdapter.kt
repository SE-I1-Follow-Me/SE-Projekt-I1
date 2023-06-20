import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.followme.R
import kotlinx.android.synthetic.main.roboter_item.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import android.content.Context
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import com.example.followme.Retrofit.RetrofitService
import com.example.followme.Retrofit.RoboterAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Klasse um die Roboter zu laden und anzeigen zu könnnen
 * @param context
 * @param robots
 *
 */
class MyAdapter(private val context: Context, private var robots: java.util.ArrayList<Robot>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    // ***** Variablen *****
    private lateinit var mListener: OnItemClickListener

    //Originaler Content für Filtern
    private var originalContent = ArrayList<Robot>()

    var filteredListShowed: Boolean = false;

    //Retrofit fuer server
    val retrofitService = RetrofitService()
    val roboterAPI = retrofitService.getRetrofit().create(RoboterAPI::class.java)

    /**
     * Funktion um den Viewholder zu erstellen
     * @param parent
     * @param viewType
     */
    // Hier wird der gerade initialisierte ViewHolder kreiert
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roboter_item, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    /**
     * Funktion um den erstellten Viewholder  zu binden, so dass er angezeigt werden kann
     * @param holder
     * @param position
     */
    // Hier wird der ViewHolder gebindet, ist son Android Ding, ist dafür da, damit es angezeit werden kann
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = robots[position]
        holder.tvRoboter.text = currentItem.robotName
        holder.tvPfeil.text = currentItem.tvPfeil

        // Set robot image based on the followme property
        holder.ivRoboter.setImageResource(if (currentItem.followme) R.drawable.b else R.drawable.a)

        holder.itemView.setOnClickListener {
            if (!filteredListShowed) {
                if (!currentItem.followme) {
                    currentItem.isMarked = !currentItem.isMarked
                    holder.ivRoboter.setImageResource(if (currentItem.isMarked) R.drawable.c else R.drawable.a)
                    holder.ivRoboter.setImageResource(if (currentItem.isMarked) R.drawable.c else R.drawable.a)
                }
            }
            else {
                if (currentItem.isMarked) {
                    currentItem.isMarked = false
                    holder.ivRoboter.setImageResource(R.drawable.b)
                } else {
                    currentItem.isMarked = true
                    holder.ivRoboter.setImageResource(R.drawable.c)
                }

            }
        }

    }


    /**
     * Klasse um die einzelnen items die in der Roboterloste sind zu initialisieren
     * @param itemView um die einzelnen Elemente eines Items zu initialisieren
     * @param mListener onClicklistener
     */
    class MyViewHolder(itemView: View, var mListener:OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // Das ist, aus was unser Roboter OBjekt besteht
        val ivRoboter : ShapeableImageView = itemView.findViewById(R.id.ivRoboter)
        val tvRoboter : TextView = itemView.findViewById(R.id.tvRoboter)
        val tvPfeil : TextView = itemView.findViewById(R.id.tvPfeil)


        //OnClick Listener
        init {
            itemView.setOnClickListener(this)
            tvPfeil.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                when (view) {
                    itemView -> mListener.setOnClickListener(position)
                    tvPfeil -> mListener.onArrowClick(position)

                }
            }
        }

    }


    // ***** Funktionen *****

    /**
     * Funktion um die Roboter von den FollowMe-Befehl abzuwählen
     */
    fun endFollowMe() {
        if (!robots.any { robot ->  robot.isMarked }) { Toast.makeText(context, "Bitte mindestens einen Roboter auswählen", Toast.LENGTH_SHORT).show()}
        robots.forEach { robot ->
            if (robot.isMarked) {
                robot.followme = false
                robot.isMarked = false
                updateFollowStatus(robot.id, robot.followme)
            }
        }
        robots = robots.filter { it.followme } as ArrayList<Robot>
        notifyDataSetChanged()
    }

    //Filter
    /**
     * Funktion um die Roboter dessen FollowMe "true" ist zu filtern
     * @param showFollowMe
     */
    fun filterItems(showFollowMe: Boolean) {

        //Befülle den originalContent einmalig
        if (originalContent.isEmpty()) {
            originalContent.addAll(robots)
        }

        //Filtere die Items
        if (showFollowMe) {

            robots.forEach { robot ->
                if (robot.isMarked) {
                    robot.followme = true
                    robot.isMarked = false
                    updateFollowStatus(robot.id, robot.followme)
                }


            }

            filteredListShowed = true

            robots = robots.filter { it.followme } as ArrayList<Robot>

        } else {
            filteredListShowed = false
            robots = ArrayList<Robot>(originalContent)
        }
        notifyDataSetChanged()
    }

    /**
     * Funktion um die Anzahl der anzuzeigenden Elemnte zu berechnen
     * @return Größe der Robot Liste
     */
    override fun getItemCount(): Int {
        return robots.size
    }

    fun addRobot(robot: Robot) {
        robots.add(robot)
        notifyItemInserted(robots.size - 1)
    }


    fun deleteRobot() {
        // robots.removeAll { robot -> robots.  }
        // notifyDatSetChanged()
    }

    //Click Listener
    interface OnItemClickListener {
        fun setOnClickListener(pos:Int)
        fun onArrowClick(position: Int)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener) {
        this.mListener = mListener
    }

    /**
     * Funktion um den Wert "FolowMe" der Roboter mittels der PATCH-Methode  zu ändern
     * @param robotId Identifier der Roboter
     * @param isFollowing
     */
    private fun updateFollowStatus(robotId: Int, isFollowing: Boolean) {
        val call = roboterAPI.updateIsFollowing(robotId, isFollowing)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Successfully updated follow status",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Failed to update follow status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Failed to update follow status",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}