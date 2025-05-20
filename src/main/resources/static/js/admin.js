// 사이드바 토글 기능 제거됨
$(document).ready(function () {
    // 드롭다운 메뉴 초기화
    var dropdowns = document.querySelectorAll('.dropdown-toggle');
    dropdowns.forEach(dropdown => {
        new bootstrap.Dropdown(dropdown);
    });

    // 툴팁 초기화
    var tooltips = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltips.map(function (tooltip) {
        return new bootstrap.Tooltip(tooltip);
    });
});

// 차트 생성 헬퍼 함수
function createChart(ctx, type, data, options) {
    return new Chart(ctx, {
        type: type,
        data: data,
        options: options || {}
    });
}

// 날짜 포맷 헬퍼 함수
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
}

// 숫자 포맷 헬퍼 함수
function formatNumber(num) {
    return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
}

// 알림 표시 함수
function showAlert(message, type) {
    var alertPlaceholder = document.getElementById('alertPlaceholder');
    if (!alertPlaceholder) {
        alertPlaceholder = document.createElement('div');
        alertPlaceholder.id = 'alertPlaceholder';
        document.querySelector('.container-fluid').prepend(alertPlaceholder);
    }

    var wrapper = document.createElement('div');
    wrapper.innerHTML = '<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
        message +
        '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
        '</div>';

    alertPlaceholder.append(wrapper);

    // 5초 후 자동으로 알림 닫기
    setTimeout(function () {
        var alert = new bootstrap.Alert(wrapper.querySelector('.alert'));
        alert.close();
    }, 5000);
}

// 데이터 테이블 초기화 함수
function initDataTable(tableId, options) {
    var table = document.getElementById(tableId);
    if (!table) return;

    // 페이지네이션 생성
    var totalItems = table.querySelectorAll('tbody tr').length;
    var itemsPerPage = options?.itemsPerPage || 10;
    var totalPages = Math.ceil(totalItems / itemsPerPage);

    if (totalPages <= 1) return;

    var paginationContainer = document.createElement('nav');
    paginationContainer.setAttribute('aria-label', 'Page navigation');

    var paginationList = document.createElement('ul');
    paginationList.className = 'pagination justify-content-center';

    // 이전 페이지 버튼
    var prevItem = document.createElement('li');
    prevItem.className = 'page-item disabled';
    var prevLink = document.createElement('a');
    prevLink.className = 'page-link';
    prevLink.href = '#';
    prevLink.setAttribute('aria-label', 'Previous');
    prevLink.innerHTML = '<span aria-hidden="true">&laquo;</span>';
    prevItem.appendChild(prevLink);
    paginationList.appendChild(prevItem);

    // 페이지 번호 버튼
    for (var i = 1; i <= totalPages; i++) {
        var pageItem = document.createElement('li');
        pageItem.className = i === 1 ? 'page-item active' : 'page-item';
        var pageLink = document.createElement('a');
        pageLink.className = 'page-link';
        pageLink.href = '#';
        pageLink.textContent = i;
        pageItem.appendChild(pageLink);
        paginationList.appendChild(pageItem);
    }

    // 다음 페이지 버튼
    var nextItem = document.createElement('li');
    nextItem.className = 'page-item';
    var nextLink = document.createElement('a');
    nextLink.className = 'page-link';
    nextLink.href = '#';
    nextLink.setAttribute('aria-label', 'Next');
    nextLink.innerHTML = '<span aria-hidden="true">&raquo;</span>';
    nextItem.appendChild(nextLink);
    paginationList.appendChild(nextItem);

    paginationContainer.appendChild(paginationList);
    table.parentNode.appendChild(paginationContainer);

    // 페이지 클릭 이벤트 처리
    var pageLinks = paginationList.querySelectorAll('.page-link');
    pageLinks.forEach(function (link) {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            var pageItems = paginationList.querySelectorAll('.page-item');
            pageItems.forEach(function (item) {
                item.classList.remove('active');
            });

            if (this.getAttribute('aria-label') === 'Previous') {
                var activeItem = paginationList.querySelector('.page-item.active');
                var prevPageItem = activeItem.previousElementSibling;
                if (prevPageItem && !prevPageItem.classList.contains('disabled')) {
                    prevPageItem.classList.add('active');
                    showTablePage(table, parseInt(prevPageItem.textContent), itemsPerPage);
                }
            } else if (this.getAttribute('aria-label') === 'Next') {
                var activeItem = paginationList.querySelector('.page-item.active');
                var nextPageItem = activeItem.nextElementSibling;
                if (nextPageItem && !nextPageItem.classList.contains('disabled')) {
                    nextPageItem.classList.add('active');
                    showTablePage(table, parseInt(nextPageItem.textContent), itemsPerPage);
                }
            } else {
                this.parentNode.classList.add('active');
                showTablePage(table, parseInt(this.textContent), itemsPerPage);
            }

            // 이전/다음 버튼 상태 업데이트
            updatePaginationButtons(paginationList, totalPages);
        });
    });

    // 초기 페이지 표시
    showTablePage(table, 1, itemsPerPage);
}

// 테이블 페이지 표시 함수
function showTablePage(table, pageNum, itemsPerPage) {
    var rows = table.querySelectorAll('tbody tr');
    var startIndex = (pageNum - 1) * itemsPerPage;
    var endIndex = startIndex + itemsPerPage;

    rows.forEach(function (row, index) {
        if (index >= startIndex && index < endIndex) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

// 페이지네이션 버튼 상태 업데이트 함수
function updatePaginationButtons(paginationList, totalPages) {
    var activeItem = paginationList.querySelector('.page-item.active');
    var currentPage = parseInt(activeItem.textContent);

    var prevItem = paginationList.querySelector('.page-item:first-child');
    var nextItem = paginationList.querySelector('.page-item:last-child');

    if (currentPage === 1) {
        prevItem.classList.add('disabled');
    } else {
        prevItem.classList.remove('disabled');
    }

    if (currentPage === totalPages) {
        nextItem.classList.add('disabled');
    } else {
        nextItem.classList.remove('disabled');
    }
}
