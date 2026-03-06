(function (global) {
  async function fetchReport(contextPath, endpoint) {
    var response = await fetch(contextPath + '/api/reports/' + endpoint);
    if (!response.ok) {
      throw new Error('Unable to load report: ' + endpoint);
    }
    return response.json();
  }

  function renderError(targetSelector, message) {
    var host = document.querySelector(targetSelector) || document.body;
    var item = document.createElement('p');
    item.className = 'notice notice-error';
    item.textContent = message;
    host.prepend(item);
  }

  global.OceanCharts = {
    fetchReport: fetchReport,
    renderError: renderError
  };
})(window);