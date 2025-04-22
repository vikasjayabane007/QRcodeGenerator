async function generateQRCode() {
    const text = document.getElementById('textInput').value;
    const qrCodeContainer = document.getElementById('qrCodeContainer');
    const loadingMessage = document.getElementById('loadingMessage');

    qrCodeContainer.innerHTML = '';
    loadingMessage.style.display = 'none';

    if (!text) {
        alert('Please enter text or a URL');
        return;
    }

    
    let timeoutId = setTimeout(() => {
        loadingMessage.style.display = 'block';
    }, 500);

    const apiUrl = "https://osq2daznmb.execute-api.us-east-1.amazonaws.com/test/qr";
    //"https://f3mvmr5ej27q563qd6lkife2em0ogntd.lambda-url.us-east-1.on.aws/";

    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ text })
        });

        const result = await response.json();
        clearTimeout(timeoutId);
        loadingMessage.style.display = 'none';

        if (typeof result === "string" && result.startsWith("data:image/png;base64,")) {
            const qrImage = new Image();
            qrImage.src = result;
            qrCodeContainer.appendChild(qrImage);
        } else {
            alert("Failed to generate QR code.");
        }
    } catch (error) {
        clearTimeout(timeoutId);
        loadingMessage.style.display = 'none';
        console.error("Error:", error);
        alert("Error generating QR code.");
    }
}
document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('textInput');
    input.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            generateQRCode();
        }
    });
});