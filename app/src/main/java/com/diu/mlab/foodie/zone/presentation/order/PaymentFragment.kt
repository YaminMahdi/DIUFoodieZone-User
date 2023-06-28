package com.diu.mlab.foodie.zone.presentation.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.diu.mlab.foodie.zone.R
import com.diu.mlab.foodie.zone.databinding.FragmentAddressBinding
import com.diu.mlab.foodie.zone.databinding.FragmentOrderConfirmationBinding
import com.diu.mlab.foodie.zone.databinding.FragmentPaymentBinding
import com.diu.mlab.foodie.zone.domain.model.OrderInfo
import com.diu.mlab.foodie.zone.util.setBounceClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {
    private lateinit var binding : FragmentPaymentBinding
    private var orderInfo = OrderInfo()

    private val viewModel by activityViewModels<OrderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)

        viewModel.orderInfo.observe(requireActivity()){
            orderInfo = it
            binding.pnNo.setText(it.userInfo.phone)
            binding.shopNm.text = it.shopInfo.nm
            binding.shopBkashNo.text = it.shopInfo.phone
            binding.money.text = "${it.typePrice * it.quantity + it.deliveryCharge}"
            binding.paymentType.text = it.shopInfo.phone

        }

        binding.btnPaymentDone.setBounceClickListener {
            viewModel.updateOrderInfo(
                varBoolName = "paid",
                value = true,
                varTimeName = "paymentTime",
                shopEmail = orderInfo.shopInfo.email,
                success = {},
                failed = {}
            )
            val newPn = binding.pnNo.text.toString()
            if(orderInfo.userInfo.phone != newPn){
                viewModel.updateUserProfile(orderInfo.userInfo.copy(phone = newPn)){}
            }
            requireActivity().supportFragmentManager
                .beginTransaction()
                .run {
                    hide(this@PaymentFragment)
                    add(R.id.orderFragment, OrderInfoFragment())
                    commit()
                }
        }
        return binding.root
    }

}