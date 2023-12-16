$(document).ready(function () {
  $("table .delete").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $("#deleteModal #deleteId").val(id);
    $.ajax({
      type: "GET",
      url: `/api/category/find/${id}`,
      success: function (category) {
        $("#deleteModal #idName2").text("Are you sure you want to remove \"" + category.categoryName + "\" from the list?");
      },
    });
  });

  $("table .edit").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $.ajax({
      type: "GET",
      url: `/api/category/find/${id}`,
      success: function (category) {
        $("#editModal #saveCategoryId").val(id);
        $("#editModal #categoryName").val(category.categoryName);
        $("#editModal #editStatus").val(category.status);
      },
    });
  });
});
