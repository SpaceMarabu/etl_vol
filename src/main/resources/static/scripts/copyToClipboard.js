function copyToClipboard(text) {
    navigator.clipboard.writeText(text)
        .then(() => {
            document.getElementById('conf-button').innerText = "COPIED";
        })
        .catch(err => {
            console.error('Не удалось скопировать текст: ', err);
        });
}