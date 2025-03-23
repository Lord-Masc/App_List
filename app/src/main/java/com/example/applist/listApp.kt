package com.example.applist

import android.R.attr.text
import android.R.id.edit
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.applist.ui.theme.AppListTheme
import java.nio.file.WatchEvent

@Composable
fun listApp(){

    var sItem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("1") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // It's is for to Add list in app
        Button(onClick = {
            showDialog = true   // if click one it than dialog box is open
        },
            modifier = Modifier.size(width = 120.dp, height = 90.dp).padding(top = 40.dp)
             ) {
            Text("Add Item")
            Spacer(modifier = Modifier.padding(2.dp))
            Icon(Icons.Default.Add, contentDescription = "Add List")

        }
        LazyColumn(
            modifier = Modifier.padding( 16.dp)
        ) {
            items(sItem){
                item ->
                if (item.isEditing){
                    ShopingItemEditor(item = item, onEditComplete = {
                        editedName , editedQuantity->
                        sItem = sItem.map { it.copy(isEditing = false) }
                        val editedItem  = sItem.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                }else{
                    ShoppingListItem(item = item , onEdit = {
                        sItem = sItem.map { it.copy(isEditing = it.id == item.id) }
                    }, onDelete = {
                        sItem = sItem.filter { it.id != item.id }
                    })

                }
            }
        }
    }
    if (showDialog){
        AlertDialog(onDismissRequest = { showDialog = false },confirmButton = {
            Row (modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = {
                    if (itemName.isNotBlank() && itemQuantity.isNotBlank() && itemQuantity.toIntOrNull()!=null){
                        val newItem = ShoppingItem(
                            id = sItem.size + 1 ,
                            name =  itemName,
                            quantity = itemQuantity.toIntOrNull() ?:1
                        )
                        sItem = sItem + newItem
                        showDialog = false
                        itemName = ""
                        itemQuantity = ""
                    }
                }) {
                    Text("Add")
                }
                Button(onClick = {
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            }
        }, title = {Text("Add Shopping Item")},
            text = {
                Column() {
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity= it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            })
    }

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AppListTheme() {
        listApp()
    }
}
data class ShoppingItem(val id:Int,
                        var name:String,
                        var quantity:Int,
                        var isEditing: Boolean = false)
@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
){
    Row(
        modifier = Modifier.padding(8.dp).fillMaxWidth().border(
             BorderStroke(2.dp , Color.Black),
             RoundedCornerShape(8.dp)
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = item.name , modifier = Modifier.padding(8.dp))
        Text(text = "Qty:${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
@Composable
fun ShopingItemEditor(item: ShoppingItem,onEditComplete:(String, Int)-> Unit){
    var EditName by remember { mutableStateOf(item.name) }
    var EditQuantity by remember {mutableStateOf(item.quantity.toString())}
    var Editng by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            BasicTextField(
                value = EditName,
                onValueChange = {EditName = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value = EditQuantity,
                onValueChange = {EditQuantity = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
        }
       Button(
           onClick = {
               val quantity = EditQuantity.toIntOrNull() ?: 1
               onEditComplete(EditName, quantity)
               Editng = false
           }
       ) {
           Text("Save")
       }
    }
}
