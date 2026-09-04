import { useState } from 'react'

function App() {
  const [url, setUrl] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [copied, setCopied] = useState(false)

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setResult(null)

    if (!url.trim()) {
      setError('Please paste a URL first.')
      return
    }

    setLoading(true)
    try {
      const response = await fetch('/api/urls', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ url: url.trim() }),
      })
      const data = await response.json()

      if (!response.ok) {
        throw new Error(data.detail || data.message || 'We could not shorten that URL.')
      }
      setResult(data)
    } catch (requestError) {
      setError(requestError.message || 'Something went wrong. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  async function copyLink() {
    await navigator.clipboard.writeText(result.shortUrl)
    setCopied(true)
    window.setTimeout(() => setCopied(false), 1800)
  }

  return (
    <div className="page-shell">
      <nav className="navbar">
        <a className="brand" href="/" aria-label="SnapLink home">
          <span className="brand-mark">↗</span>
          <span>SnapLink</span>
        </a>
        <span className="status"><i />Service online</span>
      </nav>

      <main>
        <section className="hero" aria-labelledby="page-title">
          <p className="eyebrow">A simpler way to share</p>
          <h1 id="page-title">Turn long links into<br /><em>tiny connections.</em></h1>
          <p className="hero-copy">Create a short, clean URL in seconds. Every visit is counted automatically.</p>

          <div className="shortener-card">
            <form onSubmit={handleSubmit} noValidate>
              <label htmlFor="url-input">Paste a long URL</label>
              <div className="input-row">
                <input
                  id="url-input"
                  type="url"
                  inputMode="url"
                  autoComplete="url"
                  placeholder="https://your-very-long-link.com"
                  value={url}
                  onChange={(event) => setUrl(event.target.value)}
                />
                <button type="submit" disabled={loading}>
                  {loading ? 'Creating...' : <>Shorten link <span>→</span></>}
                </button>
              </div>
              {error && <p className="form-error" role="alert">{error}</p>}
            </form>

            {result && (
              <section className="result" aria-live="polite">
                <div className="result-heading">
                  <span className="success-icon">✓</span>
                  <div>
                    <p className="result-title">Your short link is ready</p>
                    <p className="result-subtitle">Share it anywhere you like.</p>
                  </div>
                </div>
                <div className="link-box">
                  <a href={result.shortUrl} target="_blank" rel="noreferrer">{result.shortUrl}</a>
                  <button type="button" onClick={copyLink}>{copied ? 'Copied!' : 'Copy'}</button>
                </div>
                <p className="original-link">Destination: {result.originalUrl}</p>
              </section>
            )}
          </div>
        </section>

        <section className="features" aria-label="SnapLink features">
          <Feature number="01" title="Quick to create">Paste any valid destination and receive a unique short link instantly.</Feature>
          <Feature number="02" title="Built to share">Clean links that look good in messages, bios, and social posts.</Feature>
          <Feature number="03" title="Visits tracked">Every redirect is counted, so you always know your link is being used.</Feature>
        </section>
      </main>
      <footer>Made for shorter, clearer sharing.</footer>
    </div>
  )
}

function Feature({ number, title, children }) {
  return <article><span className="feature-number">{number}</span><h2>{title}</h2><p>{children}</p></article>
}

export default App
