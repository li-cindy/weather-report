package hu.ait.weatherreport

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import hu.ait.weatherreport.data.City
import kotlinx.android.synthetic.main.new_item_dialog.view.*
import java.lang.RuntimeException


class CityDialog : DialogFragment() {

    interface CityHandler {
        fun cityCreated(city: City)
    }

    private lateinit var cityHandler: CityHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is CityHandler) {
            cityHandler = context
        } else {
            throw RuntimeException(
                "The activity does not implement the ShoppingItemHandlerInterface"
            )
        }
    }

    private lateinit var etCityName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Add City")

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_item_dialog, null
        )

        etCityName = rootView.etCityName
        builder.setView(rootView)

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(
                ScrollingActivity.KEY_ITEM_TO_EDIT
            )
        ) {
            val city = arguments.getSerializable(
                ScrollingActivity.KEY_ITEM_TO_EDIT
            ) as City

            etCityName.setText(city.name)

            builder.setTitle("Edit Item")
        }

        builder.setPositiveButton("OK") { dialog, witch ->
            // empty
        }

        builder.setNegativeButton("Cancel") { dialog, witch ->
            // empty
        }
        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etCityName.text.isNotEmpty()) {
                handleCityCreate()
                dialog.dismiss()
            } else {
                etCityName.error = "This field can not be empty!"

            }
        }
    }


    private fun handleCityCreate() {
        cityHandler.cityCreated(
            City(
                null,
                etCityName.text.toString()
            )
        )
    }

}
