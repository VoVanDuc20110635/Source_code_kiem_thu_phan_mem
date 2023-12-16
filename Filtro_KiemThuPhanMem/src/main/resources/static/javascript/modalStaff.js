$(document).ready(function () {
  $("table .delete").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $("#deleteModal #deleteId").val(id);
    $.ajax({
      type: "GET",
      url: `/api/staff/find/${id}`,
      success: function (staff) {
        $("#deleteModal #idName2").text("Are you sure you want to remove \"" + staff.name + "\" from the list?");
      },
    });
  });

  $("table .edit").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $.ajax({
      type: "GET",
      url: `/api/staff/find/${id}`,
      success: function (staff) {
        $("#editModal #saveStaffId").val(id);
        $("#editModal #staffName").val(staff.name);
        const dobUTC = moment.utc(staff.dob, "YYYY/MM/DD").add(1, 'days').format("YYYY-MM-DD");
        $("#editModal #dob").val(dobUTC);
        $("#editModal #sex").val(staff.sex);
        $("#editModal #phoneNumber").val(staff.phoneNumber);
        $("#editModal #editStatus").val(staff.status);
        $("#editModal #editAccount").val(staff.account.id);
      },
    });
  });
});
