$(document).ready(function () {
  $("table .delete").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $("#deleteModal #deleteId").val(id);
    $.ajax({
      type: "GET",
      url: `/api/order/find/${id}`,
      success: function (order) {
        $("#deleteModal #idName2").text("Are you sure you want to remove order has id: \"" + order.id + "\" of customer named \"" + order.user.name +"\" from the list?");
      },
    });
  });

  $("table .edit").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $.ajax({
      type: "GET",
      url: `/api/order/find/${id}`,
      success: function (order) {
        $("#editModal #saveOrderId").val(id);
        $("#editModal #user").val(order.user.id);
        $("#editModal #orderDate").val(order.orderDate);
        $("#editModal #email").val(order.email);
        $("#editModal #phoneNumber").val(order.phoneNumber);
        $("#editModal #address").val(order.address);
        $("#editModal #city").val(order.city);
        $("#editModal #zip").val(order.zip);
        $("#editModal #paymentMethod").val(order.paymentMethod.name);
        $("#editModal #total").val(order.total);
        $("#editModal #status").val(order.status);
      },
    });
  });
});
