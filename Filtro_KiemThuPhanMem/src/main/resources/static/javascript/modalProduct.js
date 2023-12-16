$(document).ready(function () {
  $("table .delete").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $("#deleteModal #deleteId").val(id);
    $.ajax({
      type: "GET",
      url: `/api/product/find/${id}`,
      success: function (product) {
        $("#deleteModal #idName2").text("Are you sure you want to remove \"" + product.productName + "\" from the list?");
      },
    });
  });

  $("table .edit").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $.ajax({
      type: "GET",
      url: `/api/product/find/${id}`,
      success: function (product) {
        $("#editModal #saveProductId").val(id);
        $("#editModal #productName").val(product.productName);
        $("#editModal #price").val(product.price);
        $("#editModal #quantity").val(product.quantity);
        $("#editModal #sold").val(product.sold);
        $("#editModal #description").val(product.description);
        $("#editModal #image").val(product.image);
        $("#editModal #editStatus").val(product.status);
        $("#editModal #discount").val(product.discount);
        $("#editModal #editMaterial").val(product.material.id);
        $("#editModal #editCategory").val(product.category.id);
      },
    });
  });
});
