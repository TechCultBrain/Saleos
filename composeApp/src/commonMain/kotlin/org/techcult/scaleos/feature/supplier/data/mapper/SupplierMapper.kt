package org.techcult.scaleos.feature.supplier.data.mapper

import org.techcult.scaleos.feature.supplier.data.local.entity.SupplierEntity
import org.techcult.scaleos.feature.supplier.data.local.entity.SupplierWithPurchaseStats
import org.techcult.scaleos.feature.supplier.domain.model.Supplier


fun Supplier.toSupplierEntity(): SupplierEntity {
    return SupplierEntity(
        supplierId = supplierId,
        supplierName = supplierName,
        supplierCode = supplierCode,
        supplierType = supplierType,
        supplierNotes = supplierNotes,
        contactPerson = contactPerson,
        contactNumber = contactNumber,
        wssNumber = wssNumber,
        upiId = upiId,
        emailId = email,
        gstNumber = gstNumber,
        address = address,
        city = city,
        state = state,
        pinCode = pinCode,
        openingBalance = openingBalance,
        supplyingBrands = supplyingBrands,
        isAvailable = isAvailable,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
        updatedBy = updatedBy
    )
}

fun SupplierEntity.toSupplier(): Supplier {
    return Supplier(
        supplierId = supplierId,
        supplierName = supplierName,
        contactNumber = contactNumber,
        wssNumber = wssNumber,
        upiId = upiId,
        email = emailId,
        gstNumber = gstNumber,
        address = address,
        city = city,
        state = state,
        pinCode = pinCode,
        openingBalance = openingBalance,
        supplyingBrands = supplyingBrands,
        isAvailable = isAvailable,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdBy = createdBy,
        updatedBy = updatedBy,
        supplierCode = supplierCode,
        supplierNotes = supplierNotes,
        supplierType = supplierType,
        contactPerson = contactPerson
    )
}

fun SupplierWithPurchaseStats.toSupplier(): Supplier {
    return Supplier(
        supplierId = supplier.supplierId,
        supplierName = supplier.supplierName,
        contactNumber = supplier.contactNumber,
        wssNumber = supplier.wssNumber,
        upiId = supplier.upiId,
        email = supplier.emailId,
        gstNumber = supplier.gstNumber,
        address = supplier.address,
        city = supplier.city,
        state = supplier.state,
        pinCode = supplier.pinCode,
        openingBalance = supplier.openingBalance,
        supplyingBrands = supplier.supplyingBrands,
        isAvailable = supplier.isAvailable,
        createdAt = supplier.createdAt,
        updatedAt = supplier.updatedAt,
        createdBy = supplier.createdBy,
        updatedBy = supplier.updatedBy,
        supplierCode = supplier.supplierCode,
        supplierNotes = supplier.supplierNotes,
        supplierType = supplier.supplierType,
        contactPerson = supplier.contactPerson,
        lastOrderDate = lastPurchaseDate,
        totalPurchases = totalPurchaseAmount
    )
}