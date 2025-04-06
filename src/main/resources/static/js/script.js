// 메뉴 관련 JavaScript 기능
document.addEventListener('DOMContentLoaded', function () {
    // 하위 메뉴 토글 기능
    const menuWithSubmenu = document.querySelectorAll('.menu-with-submenu > span');

    menuWithSubmenu.forEach(menu => {
        menu.addEventListener('click', function () {
            const submenu = this.nextElementSibling;
            submenu.style.display = submenu.style.display === 'none' ? 'block' : 'none';
        });
    });

    // 페이지 로드 시 현재 페이지에 해당하는 메뉴 활성화
    highlightCurrentPageMenu();
});

// 현재 페이지 메뉴 하이라이트
function highlightCurrentPageMenu() {
    const currentPath = window.location.pathname;
    const menuLinks = document.querySelectorAll('.sidebar-menu a');

    menuLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');

            // 부모 서브메뉴가 있다면 열기
            const parentSubmenu = link.closest('.submenu');
            if (parentSubmenu) {
                parentSubmenu.style.display = 'block';
            }
        }
    });
}
