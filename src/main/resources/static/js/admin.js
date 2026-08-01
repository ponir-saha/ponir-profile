(() => {
    const title = document.querySelector('[data-slug-source]');
    const slug = document.querySelector('[data-slug-target]');
    if (!title || !slug) return;

    let manuallyEdited = Boolean(slug.value);
    slug.addEventListener('input', () => { manuallyEdited = Boolean(slug.value); });
    title.addEventListener('input', () => {
        if (manuallyEdited) return;
        slug.value = title.value
            .normalize('NFD').replace(/[\u0300-\u036f]/g, '')
            .toLowerCase().replace(/[^a-z0-9]+/g, '-')
            .replace(/^-|-$/g, '');
    });
})();
