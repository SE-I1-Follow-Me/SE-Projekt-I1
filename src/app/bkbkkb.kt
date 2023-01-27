import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.followme.
import kotlinx.android.synthetic.main.roboter_item.view.*

class RobotAdapter(private val robots: MutableList<Robot>) : RecyclerView.Adapter<RobotAdapter.RobotViewHolder>()