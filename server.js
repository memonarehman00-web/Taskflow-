const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const PUBLIC_DIR = path.join(__dirname, 'public');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.js': 'application/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.apk': 'application/vnd.android.package-archive',
  '.zip': 'application/zip'
};

const server = http.createServer((req, res) => {
  // Normalize URL
  let reqPath = req.url.split('?')[0];
  if (reqPath === '/') reqPath = '/index.html';

  // Check in public/ first, then root __dirname
  let filePath = path.join(PUBLIC_DIR, reqPath);
  if (!fs.existsSync(filePath)) {
    filePath = path.join(__dirname, reqPath);
  }

  // Handle manifest explicitly
  if (reqPath === '/manifest.json') {
    res.setHeader('Content-Type', 'application/manifest+json; charset=utf-8');
    res.setHeader('Access-Control-Allow-Origin', '*');
  }

  if (fs.existsSync(filePath) && fs.statSync(filePath).isFile()) {
    const ext = path.extname(filePath).toLowerCase();
    const contentType = reqPath === '/manifest.json' 
      ? 'application/manifest+json; charset=utf-8' 
      : (MIME_TYPES[ext] || 'application/octet-stream');
    
    res.setHeader('Content-Type', contentType);
    res.setHeader('Access-Control-Allow-Origin', '*');
    
    if (ext === '.apk' || ext === '.zip') {
      res.setHeader('Content-Disposition', `attachment; filename="${path.basename(filePath)}"`);
    }

    const stream = fs.createReadStream(filePath);
    res.statusCode = 200;
    stream.pipe(res);
  } else {
    // Fallback to index.html for SPA
    const indexPath = path.join(PUBLIC_DIR, 'index.html');
    if (fs.existsSync(indexPath)) {
      res.statusCode = 200;
      res.setHeader('Content-Type', 'text/html; charset=utf-8');
      fs.createReadStream(indexPath).pipe(res);
    } else {
      res.statusCode = 404;
      res.end('Not Found');
    }
  }
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`Server listening on port ${PORT}`);
});
