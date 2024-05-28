function closeAlertSu() {
    document.querySelector(".alert").classList.add("close-alert-su");
  }

  function closeAlertEr() {
    document.querySelector(".alert-error").classList.add("close-alert-er");
  }

function openFilter() {
  document.querySelector(".filter-all").classList.add("open-filter-all");
  document.querySelector(".btn-filter").classList.add("btn-filter-on");
}

function closeFilter() {
  document.querySelector(".filter-all").classList.remove("open-filter-all");
  document.querySelector(".btn-filter").classList.remove("btn-filter-on");
}