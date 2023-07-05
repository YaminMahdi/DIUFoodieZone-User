package com.diu.mlab.foodie.zone.data.repo

import com.diu.mlab.foodie.zone.domain.model.OrderInfo
import com.diu.mlab.foodie.zone.domain.repo.OrderRepo
import com.diu.mlab.foodie.zone.util.transformedEmailId
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class OrderRepoImpl @Inject constructor(
    private val realtime: FirebaseDatabase,
    private val firebaseUser: FirebaseUser?,
    ): OrderRepo {
    private val userEmail = firebaseUser?.email?.transformedEmailId().toString()
    override fun getOrderInfo(
        orderId: String,
        success: (orderInfo: OrderInfo) -> Unit,
        failed: (msg: String) -> Unit
    ) {
        if(firebaseUser != null) {
            realtime
                .getReference("orderInfo/all").child(userEmail).child(orderId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val info = snapshot.getValue<OrderInfo>()
                        success.invoke(info ?: OrderInfo())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        failed.invoke(error.message)
                    }
                })
        }
    }

    override fun getMyOrderList(
        success: (orderInfoList: List<OrderInfo>) -> Unit,
        failed: (msg: String) -> Unit
    ) {
        val myOrderList = mutableListOf<OrderInfo>()
        realtime
            .getReference("orderInfo/all").child(userEmail)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val info = snapshot.getValue<OrderInfo>()!!
                    myOrderList.add(0,info)
                    success.invoke(myOrderList)
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    val info = snapshot.getValue<OrderInfo>()!!
                    myOrderList.forEachIndexed { index, orderInfo ->
                        if(orderInfo.orderId == previousChildName){
                            success.invoke(myOrderList.toMutableList().apply {
                                removeAt(index)
                                add(index,info)
                            })
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {
                    failed.invoke(error.message)
                }
            })
    }

    override fun placeOrder(
        orderInfo: OrderInfo,
        success: (orderInfo: OrderInfo) -> Unit,
        failed: (msg: String) -> Unit
    ) {
        if(firebaseUser != null) {
            val ref = realtime.getReference("orderInfo/current")
            val orderId = ref.push().key!!
            val info =
                orderInfo
                    .copy(
                        orderId = orderId,
                        isOrdered = true,
                        orderTime = System.currentTimeMillis()
                    )

            realtime
                .getReference("orderInfo/all")
                .child(userEmail)
                .child(orderId)
                .setValue(info)
                .addOnFailureListener {
                    failed.invoke(it.message.toString())
                }

            realtime
                .getReference("orderInfo/shop")
                .child(orderInfo.shopInfo.email)
                .child("current")
                .child(orderId)
                .setValue(info)
                .addOnFailureListener {
                    failed.invoke(it.message.toString())
                }

            ref.child(orderId)
                .setValue(info)
                .addOnSuccessListener {
                    success.invoke(info)
                }
                .addOnFailureListener {
                    failed.invoke(it.message.toString())
                }
        }
    }

    override fun updateOrderInfo(
        orderId: String,
        varBoolName: String,
        value: Boolean,
        varTimeName: String,
        shopEmail: String,
        success: () -> Unit,
        failed: (msg: String) -> Unit
    ) {
        if(firebaseUser != null) {
            realtime
                .getReference("orderInfo/all")
                .child(userEmail)
                .child(orderId)
                .child(varBoolName)
                .setValue(value)

            realtime
                .getReference("orderInfo/current")
                .child(orderId)
                .child(varBoolName)
                .setValue(value)

            realtime
                .getReference("orderInfo/shop")
                .child(shopEmail)
                .child("current")
                .child(orderId)
                .child(varBoolName)
                .setValue(value)
                .addOnSuccessListener {
                    success.invoke()
                }
                .addOnFailureListener {
                    failed.invoke(it.message.toString())
                }


            val time = System.currentTimeMillis()
            realtime
                .getReference("orderInfo/all")
                .child(userEmail)
                .child(orderId)
                .child(varTimeName)
                .setValue(time)
            realtime
                .getReference("orderInfo/current")
                .child(orderId)
                .child(varTimeName)
                .setValue(time)
            realtime
                .getReference("orderInfo/shop")
                .child(shopEmail)
                .child("current")
                .child(orderId)
                .child(varTimeName)
                .setValue(time)
        }
    }
}