package com.example.cryptoapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentDetailsBinding
import com.example.cryptoapp.models.CryptoCurrency

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding
    private val item: DetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        var data: CryptoCurrency = item.data!!

        setupDetails(data)
        loadChart(data)
        setButton(data)

        return binding.root
    }

    private fun setButton(item: CryptoCurrency) {
        val oneMonth = binding.button
        val oneDay = binding.button2
        val oneWeek = binding.button1
        val oneHour = binding.button4
        val fourHour = binding.button3
        val fifteenMinute = binding.button5

        val clickListener = View.OnClickListener {
            when(it.id){
                fifteenMinute.id->loadChartData(it,"15",item,oneDay,oneWeek,fourHour,oneMonth,oneHour)
                oneDay.id->loadChartData(it,"D",item,fifteenMinute,oneWeek,fourHour,oneMonth,oneHour)
                oneWeek.id->loadChartData(it,"W",item,oneDay,fifteenMinute,fourHour,oneMonth,oneHour)
                oneMonth.id->loadChartData(it,"M",item,oneDay,oneWeek,fourHour,fifteenMinute,oneHour)
                oneHour.id->loadChartData(it,"1H",item,oneDay,oneWeek,fourHour,oneMonth,fifteenMinute)
                fourHour.id->loadChartData(it,"4H",item,oneDay,oneWeek,fifteenMinute,oneMonth,oneHour)
            }
        }

        fifteenMinute.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
    }

    private fun loadChartData(
        it: View?,
        s: String,
        item: CryptoCurrency,
        oneDay: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneMonth: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        disableButton(oneDay,oneWeek,fourHour,oneMonth,oneHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol="+ item.symbol
                .toString() + "USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg="+
                    "F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features="+
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun disableButton(oneDay: AppCompatButton, oneWeek: AppCompatButton, fourHour: AppCompatButton, oneMonth: AppCompatButton, oneHour: AppCompatButton) {
        oneDay.background =null
        oneWeek.background =null
        fourHour.background =null
        oneMonth.background =null
        oneHour.background =null
    }

    private fun loadChart(item: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol="+ item.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg="+
                    "F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features="+
                    "[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun setupDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text = data.symbol
        Glide.with(requireContext()).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+ data.id
                +".png").thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)
        binding.detailPriceTextView.text ="${String.format("$%.7f",data.quotes[0].price)}"

        if(data.quotes[0].percentChange24h>0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+${String.format("%.02f",data.quotes[0].percentChange24h)} %"
        }
        else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.rock_red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text = "${String.format("%.02f",data.quotes[0].percentChange24h)} %"
        }

    }
}