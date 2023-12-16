$(document).ready(function () {
  // $("a.edit").on("click", function () {
  //   $("#editAccountModal").modal("show"); // Hiển thị modal khi nhấp vào nút a có lớp "edit"
  // });
  $("table .delete").on("click", function () {
    const id = $(this).parent().find("#id").val();
    // alert(id);
    $("#deleteModal #deleteId").val(id);
    // $("#deleteModal #idName2").text("Are you sure you want to remove from the list?");
    $.ajax({
      type: "GET",
      url: `/api/account/find/${id}`,
      success: function (account) {
        $("#deleteModal #idName2").text("Are you sure you want to remove \"" + account.accountName + "\" from the list?");
      },
    });
  });

  $("table .edit").on("click", function () {
    const id = $(this).parent().find("#id").val();
    // alert(id);
    $.ajax({
      type: "GET",
      url: `/api/account/find/${id}`,
      success: function (account) {
        $("#editModal #saveAccountId").val(id);
        $("#editModal #accountName").val(account.accountName);
        $("#editModal #password").val(account.password);
        $("#editModal #editStatus").val(account.status);
        $("#editModal #editRole").val(account.roleNumber);
      },
    });
  });
});
