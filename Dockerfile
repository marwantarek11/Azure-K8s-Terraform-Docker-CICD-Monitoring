FROM python:3.12-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/* && \
    useradd --create-home --shell /bin/bash appuser

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt && rm -rf /root/.cache

COPY . .

RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 5000

# Tell Flask where the app is
ENV FLASK_APP=run.py

# Bind to all interfaces
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_RUN_PORT=5000

# Use FLASK_DEBUG instead of FLASK_ENV
ENV FLASK_DEBUG=False

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:5000/health || exit 1

# Use flask CLI
CMD ["flask", "run", "--host=0.0.0.0", "--port=5000"]