package org.techcult.scaleos.core.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.techcult.scaleos.core.presentation.components.MyTextField
import org.techcult.scaleos.core.presentation.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProductForm()
        }
    }
}

@Composable
fun ProductForm() {
    Column(
        modifier = Modifier.fillMaxWidth(0.5f).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicInformationCard()
        TextileSpecificationsCard()
        PricingAndProfitCard()
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //Icon(imageVector = icon, contentDescription = null)
        Text(text = title, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun BasicInformationCard() {
    var productName by remember { mutableStateOf(TextFieldValue("")) }
    var sku by remember { mutableStateOf(TextFieldValue("")) }
    var barcode by remember { mutableStateOf(TextFieldValue("")) }
    var category by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var tags by remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(0.5f)),
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader("Basic Information")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MyTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = "Product Name *",
                    placeholder = "Enter product name",
                    modifier = Modifier.weight(1f)
                )
                MyTextField(
                    value = sku,
                    onValueChange = { sku = it },
                    label = "SKU / Product Code *",
                    placeholder = "e.g., PROD-001",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MyTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = "Barcode / QR Code",
                    placeholder = "Scan or enter barcode",
                    onTrailingIconClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )
                MyTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = "Category *",
                    placeholder = "Select category",
                    modifier = Modifier.weight(1f)
                )
            }
            MyTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                placeholder = "Enter product description, features, specifications...",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )
            MyTextField(
                value = tags,
                onValueChange = { tags = it },
                label = "Tags",
                placeholder = "e.g., organic, premium, bestseller (comma-separated)",
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = "Use tags to help categorize and search for products",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun TextileSpecificationsCard() {
    var fabricType by remember { mutableStateOf(TextFieldValue("")) }
    var color by remember { mutableStateOf(TextFieldValue("")) }
    var size by remember { mutableStateOf(TextFieldValue("")) }
    var gsm by remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(0.5f)),
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader("Textile Specifications") // Replace with appropriate icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MyTextField(
                    value = fabricType,
                    onValueChange = { fabricType = it },
                    label = "Fabric Type",
                    placeholder = "e.g., Cotton, Polyester, Silk",
                    modifier = Modifier.weight(1f)
                )
                MyTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = "Color",
                    placeholder = "e.g., Navy Blue, Red",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MyTextField(
                    value = size,
                    onValueChange = { size = it },
                    label = "Size / Dimensions",
                    placeholder = "e.g., 2m x 3m, XL",
                    modifier = Modifier.weight(1f)
                )
                MyTextField(
                    value = gsm,
                    onValueChange = { gsm = it },
                    label = "GSM / Thread Count",
                    placeholder = "e.g., 200 GSM",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun PricingAndProfitCard() {
    var calculateFromProfit by remember { mutableStateOf(true) }
    var costPrice by remember { mutableStateOf(TextFieldValue("95")) }
    var desiredProfit by remember { mutableStateOf(TextFieldValue("10")) }
    val calculatedSellingPrice = "104.5"

    Surface(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(0.5f)),
        color = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeader("Pricing & Profit")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Calculate selling price from profit %")

                }
                Switch(
                    checked = calculateFromProfit,
                    onCheckedChange = { calculateFromProfit = it }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MyTextField(
                    value = costPrice,
                    onValueChange = { costPrice = it },
                    label = "Cost Price (Purchase Price) *",
                    modifier = Modifier.weight(1f)
                )
                MyTextField(
                    value = desiredProfit,
                    onValueChange = { desiredProfit = it },
                    label = "Desired Profit % *",
                    suffix = "%",
                    modifier = Modifier.weight(1f)
                )
                Column(modifier = Modifier.weight(1f).padding(top = 8.dp)) {
                    Text(
                        text = "Calculated Selling Price *",
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = calculatedSellingPrice,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}
