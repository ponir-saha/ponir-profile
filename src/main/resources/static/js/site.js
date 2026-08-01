(() => {
    const root = document.documentElement;
    const body = document.body;
    const themeButton = document.querySelector('[data-theme-toggle]');
    const openButton = document.querySelector('[data-menu-open]');
    const closeButtons = document.querySelectorAll('[data-menu-close]');

    themeButton?.addEventListener('click', () => {
        const next = root.dataset.theme === 'light' ? 'dark' : 'light';
        root.dataset.theme = next;
        localStorage.setItem('portfolio-theme', next);
    });

    openButton?.addEventListener('click', () => body.classList.add('menu-open'));
    closeButtons.forEach(button => button.addEventListener('click', () => body.classList.remove('menu-open')));
    window.addEventListener('keydown', event => {
        if (event.key === 'Escape') body.classList.remove('menu-open');
    });
})();
