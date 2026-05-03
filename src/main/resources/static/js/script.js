document.addEventListener('DOMContentLoaded', () => {
    // Theme Toggle Logic
    const themeToggleBtn = document.getElementById('theme-toggle');
    const body = document.body;

    // Check saved theme
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        body.setAttribute('data-theme', savedTheme);
        updateIcon(savedTheme);
    }

    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', () => {
            const currentTheme = body.getAttribute('data-theme');
            const newTheme = currentTheme === 'dark' ? 'light' : 'dark';

            body.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            updateIcon(newTheme);
        });
    }

    function updateIcon(theme) {
        if (themeToggleBtn) {
            themeToggleBtn.textContent = theme === 'dark' ? '☀️' : '🌙';
        }
    }

    // AI Chatbot Logic
    initChatbot();
});

function initChatbot() {
    const chatbotHTML = `
        <div id="chatbot-widget" style="position: fixed; bottom: 20px; right: 20px; z-index: 9999; font-family: sans-serif;">
            <button id="chatbot-toggle" style="width: 60px; height: 60px; border-radius: 50%; background: linear-gradient(135deg, var(--primary-color), var(--accent-color)); color: white; border: none; box-shadow: 0 4px 12px rgba(0,0,0,0.3); font-size: 24px; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: transform 0.3s;">
                🤖
            </button>
            <div id="chatbot-window" style="display: none; position: absolute; bottom: 80px; right: 0; width: 350px; height: 450px; background: var(--card-bg); border-radius: 12px; box-shadow: 0 8px 24px rgba(0,0,0,0.2); flex-direction: column; overflow: hidden; border: 1px solid rgba(0,0,0,0.1);">
                <div style="background: linear-gradient(135deg, var(--primary-color), var(--accent-color)); color: white; padding: 15px; display: flex; justify-content: space-between; align-items: center;">
                    <h4 style="margin: 0; font-size: 16px;">Crowd Assist AI</h4>
                    <button id="chatbot-close" style="background: none; border: none; color: white; cursor: pointer; font-size: 16px;">✖</button>
                </div>
                <div id="chatbot-messages" style="flex: 1; padding: 15px; overflow-y: auto; display: flex; flex-direction: column; gap: 10px; background: var(--bg-color);">
                    <div style="background: var(--card-bg); color: var(--text-color); padding: 10px 14px; border-radius: 12px 12px 12px 0; align-self: flex-start; max-width: 80%; font-size: 14px; box-shadow: 0 2px 4px rgba(0,0,0,0.05);">
                        Hello! I am your Crowd Assist AI. Ask me about live tracking, QR entry, or system guidance.
                    </div>
                </div>
                <div style="padding: 10px; background: var(--card-bg); border-top: 1px solid rgba(0,0,0,0.1); display: flex; gap: 8px;">
                    <input type="text" id="chatbot-input" placeholder="Type your message..." style="flex: 1; padding: 10px; border: 1px solid var(--secondary-color); border-radius: 20px; outline: none; background: var(--bg-color); color: var(--text-color);">
                    <button id="chatbot-send" style="background: var(--primary-color); color: white; border: none; padding: 0 15px; border-radius: 20px; cursor: pointer; font-weight: bold;">Send</button>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', chatbotHTML);

    const toggleBtn = document.getElementById('chatbot-toggle');
    const closeBtn = document.getElementById('chatbot-close');
    const chatWindow = document.getElementById('chatbot-window');
    const sendBtn = document.getElementById('chatbot-send');
    const inputField = document.getElementById('chatbot-input');
    const messagesContainer = document.getElementById('chatbot-messages');

    toggleBtn.addEventListener('click', () => {
        chatWindow.style.display = chatWindow.style.display === 'none' ? 'flex' : 'none';
        toggleBtn.style.transform = chatWindow.style.display === 'flex' ? 'scale(0.9)' : 'scale(1)';
    });

    closeBtn.addEventListener('click', () => {
        chatWindow.style.display = 'none';
        toggleBtn.style.transform = 'scale(1)';
    });

    function addMessage(text, isUser) {
        const align = isUser ? 'flex-end' : 'flex-start';
        const radius = isUser ? '12px 12px 0 12px' : '12px 12px 12px 0';
        const bg = isUser ? 'var(--primary-color)' : 'var(--card-bg)';
        const color = isUser ? 'white' : 'var(--text-color)';
        
        const msgHTML = `
            <div style="background: ${bg}; color: ${color}; padding: 10px 14px; border-radius: ${radius}; align-self: ${align}; max-width: 80%; font-size: 14px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); animation: fadeIn 0.3s;">
                ${text}
            </div>
        `;
        messagesContainer.insertAdjacentHTML('beforeend', msgHTML);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    function processAIResponse(msg) {
        const lowerMsg = msg.toLowerCase();
        let response = "I'm sorry, I didn't understand that. You can ask about crowd tracking, generating QR passes, or view dashboard metrics.";
        
        if (lowerMsg.includes("crowd") || lowerMsg.includes("track")) {
            response = "You can view live crowd levels on our <a href='/live-tracking' style='color: inherit; text-decoration: underline;'>Live Tracking</a> page. City Center is currently at 85% capacity.";
        } else if (lowerMsg.includes("qr") || lowerMsg.includes("entry")) {
            response = "To enter a monitored zone, please generate a QR pass at the <a href='/qr-entry' style='color: inherit; text-decoration: underline;'>QR Entry</a> page.";
        } else if (lowerMsg.includes("admin") || lowerMsg.includes("dashboard")) {
            response = "The dashboard provides real-time analytics on verified problems and crowd flow. You need admin credentials to view full details.";
        } else if (lowerMsg.includes("hello") || lowerMsg.includes("hi")) {
            response = "Hello! How can I assist you with the Crowd Management System today?";
        }

        setTimeout(() => addMessage(response, false), 600);
    }

    sendBtn.addEventListener('click', () => {
        const text = inputField.value.trim();
        if (text) {
            addMessage(text, true);
            inputField.value = '';
            processAIResponse(text);
        }
    });

    inputField.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            sendBtn.click();
        }
    });
}
