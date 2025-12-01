package org.techcult.scaleos.core.utils

enum class PaymentTerms(val value: String) {
    CASH_ON_DELIVERY("Cash on Delivery"),
    PrePaid("Pre-Paid"),
    PostPaid("Post-Paid"),
    Net30Days("Net 30 Days"),
    Net60Days("Net 60 Days"),
    Net90Days("Net 90 Days"),
    Net120Days("Net 120 Days")
}