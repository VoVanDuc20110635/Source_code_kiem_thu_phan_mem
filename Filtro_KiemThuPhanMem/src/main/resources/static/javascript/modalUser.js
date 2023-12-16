$(document).ready(function () {
  $("table .delete").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $("#deleteModal #deleteId").val(id);
    $.ajax({
      type: "GET",
      url: `/api/user/find/${id}`,
      success: function (user) {
        $("#deleteModal #idName2").text("Are you sure you want to remove \"" + user.name + "\" from the list?");
      },
    });
  });

  $("table .edit").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $.ajax({
      type: "GET",
      url: `/api/user/find/${id}`,
      success: function (user) {
        $("#editModal #saveUserId").val(id);
        $("#editModal #name").val(user.name);
        $("#editModal #email").val(user.email);
        $("#editModal #phoneNumber").val(user.phoneNumber);
        $("#editModal #city").val(user.city);
        $("#editModal #zip").val(user.zip);
        $("#editModal #address").val(user.address);
        $("#editModal #sex").val(user.sex);
        const dobUTC = moment.utc(user.dob, "YYYY/MM/DD").add(1, 'days').format("YYYY-MM-DD");
        $("#editModal #dob").val(dobUTC);
        // $("#editModal #dob").val(user.dob);
        $("#editModal #account").val(user.account.id);
      },
    });
  });
});
