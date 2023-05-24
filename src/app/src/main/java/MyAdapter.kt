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

//Suuuper nervige Sache
// Falls etwas unklar ist, bitte RecyclerView-Tutorial mal anschauen
class MyAdapter(private val context: Context, private var robots: java.util.ArrayList<Robot>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    // ***** Variablen *****
    private lateinit var mListener: OnItemClickListener

    //Originaler Content für Filtern
    private var originalContent = ArrayList<Robot>()

    var filteredListShowed: Boolean = false;

    // Hier wird der gerade initialisierte ViewHolder kreiert
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roboter_item, parent, false)
        return MyViewHolder(itemView, mListener)
    }


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



    //Basically wird hier angegben welche Items, der RecyclerView halten soll
    class MyViewHolder(itemView: View, var mListener:OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // Das ist, aus was unser Roboter OBjekt besteht
        val ivRoboter : ShapeableImageView = itemView.findViewById(R.id.ivRoboter)
        val tvRoboter : TextView = itemView.findViewById(R.id.tvRoboter)
        val tvPfeil : TextView = itemView.findViewById(R.id.tvPfeil)


        //OnClick Listener
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if(mListener != null) {
                mListener.setOnClickListener(absoluteAdapterPosition)
            }
        }

    }


    // ***** Funktionen *****

    //Beende FollowMe
    fun endFollowMe() {
        if (!robots.any { robot ->  robot.isMarked }) { Toast.makeText(context, "Bitte mindestens einen Roboter auswählen", Toast.LENGTH_SHORT).show()}
        robots.forEach { robot ->
            if (robot.isMarked) {
                robot.followme = false
                robot.isMarked = false
            }
        }
        robots = robots.filter { it.followme } as ArrayList<Robot>
        notifyDataSetChanged()
    }

    //Filter
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
    }

    fun setOnItemClickListener(mListener: OnItemClickListener) {
        this.mListener = mListener
    }


}