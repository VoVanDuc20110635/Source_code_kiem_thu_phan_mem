$(document).ready(function () {
  $("table .delete").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $("#deleteModal #deleteId").val(id);
    $.ajax({
      type: "GET",
      url: `/api/material/find/${id}`,
      success: function (material) {
        $("#deleteModal #idName2").text("Are you sure you want to remove \"" + material.materialName + "\" from the list?");
      },
    });
  });

  $("table .edit").on("click", function () {
    const id = $(this).parent().find("#id").val();
    $.ajax({
      type: "GET",
      url: `/api/material/find/${id}`,
      success: function (material) {
        $("#editModal #saveId").val(id);
        $("#editModal #materialName").val(material.materialName);
        $("#editModal #description").val(material.description);
        $("#editModal #editStatus").val(material.status);
      },
    });
  });
});
