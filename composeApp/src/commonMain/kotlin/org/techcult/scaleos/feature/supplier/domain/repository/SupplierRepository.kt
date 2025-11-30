package org.techcult.scaleos.feature.supplier.domain.repository

import androidx.room.Upsert
import com.techcult.salesman.core.domain.DataError
import com.techcult.salesman.core.domain.Result
import kotlinx.coroutines.flow.Flow
import org.techcult.scaleos.feature.supplier.domain.model.Supplier

interface SupplierRepository {


    suspend fun upsertSupplier(supplier: Supplier): Result<String, DataError.Local>
    fun observeFilteredSupplier(query: String?, statusFilter: Int): Flow<List<Supplier>>

}