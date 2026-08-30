const CACHE_NAME = 'budget-app-v1';

// Install event
self.addEventListener('install', (event) => {
    console.log('[Service Worker] Installed');
    self.skipWaiting();
});

// Activate event
self.addEventListener('activate', (event) => {
    console.log('[Service Worker] Activated');
    event.waitUntil(clients.claim());
});

// Fetch event (Network First strategy)
self.addEventListener('fetch', (event) => {
    event.respondWith(
        fetch(event.request).catch(() => {
            return caches.match(event.request).then((response) => {
                if (response) {
                    return response;
                }
                // Return a valid fallback response if offline and not cached
                return new Response('Offline', {
                    status: 503,
                    statusText: 'Service Unavailable'
                });
            });
        })
    );
});