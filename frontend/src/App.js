import React, { useState } from "react";
import "./App.css";

const API_BASE = "http://localhost:8080/demo_war_exploded/api";

async function fetchJson(path, options = {}) {
    const res = await fetch(API_BASE + path, {
        headers: { "Content-Type": "application/json", ...(options.headers || {}) },
        ...options,
    });

    const text = await res.text();
    if (!res.ok) {
        throw new Error(res.status + " " + res.statusText + " – " + text);
    }

    try {
        return JSON.parse(text);
    } catch {
        return text;
    }
}

function App() {
    const [authors, setAuthors] = useState([]);
    const [newAuthorName, setNewAuthorName] = useState("");

    const [books, setBooks] = useState([]);
    const [newBookTitle, setNewBookTitle] = useState("");
    const [newBookAuthorId, setNewBookAuthorId] = useState("");

    const [title, setTitle] = useState("");
    const [externalBooks, setExternalBooks] = useState([]);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleRequest = async (fn) => {
        setError("");
        setLoading(true);
        try {
            await fn();
        } catch (e) {
            console.error(e);
            setError(e.message);
        } finally {
            setLoading(false);
        }
    };

    const loadAuthors = () =>
        handleRequest(async () => {
            const data = await fetchJson("/authors");
            setAuthors(data);
        });

    const createAuthor = () =>
        handleRequest(async () => {
            if (!newAuthorName.trim()) return;
            const author = await fetchJson("/authors", {
                method: "POST",
                body: JSON.stringify({ name: newAuthorName }),
            });
            setAuthors((prev) => [...prev, author]);
            setNewAuthorName("");
        });

    const loadBooks = () =>
        handleRequest(async () => {
            const data = await fetchJson("/books");
            setBooks(data);
        });

    const createBook = () =>
        handleRequest(async () => {
            if (!newBookTitle.trim() || !newBookAuthorId.trim()) return;

            const bookPayload = {
                title: newBookTitle,
                authorId: parseInt(newBookAuthorId)
            };

            const book = await fetchJson("/books", {
                method: "POST",
                body: JSON.stringify(bookPayload),
            });

            setBooks((prev) => [...prev, book]);
            setNewBookTitle("");
            setNewBookAuthorId("");
        });

    const searchOpenLibrary = () =>
        handleRequest(async () => {
            if (!title.trim()) return;
            const data = await fetchJson(
                "/openlibrary/search?title=" + encodeURIComponent(title)
            );
            setExternalBooks(data);
        });

    return (
        <div className="page">
            <header className="header">
                <div className="header-main">
                    <div className="header-logo">EB</div>
                    <div>
                        <h1 className="header-title">E-Business Library Dashboard</h1>
                    </div>
                </div>
            </header>

            <main className="content">
                {loading && <p className="status status-loading">Se încarcă...</p>}
                {error && (
                    <p className="status status-error">
                        <strong>Eroare:</strong> {error}
                    </p>
                )}

                <div className="grid">
                    {/* AUTORI */}
                    <section className="card">
                        <div className="card-header">
                            <div className="card-icon authors">A</div>
                            <div>
                                <h2 className="card-title">Autori</h2>
                                <p className="card-subtitle">
                                    Vizualizează și adaugă autori în sistem.
                                </p>
                            </div>
                        </div>

                        <div className="card-actions">
                            <button onClick={loadAuthors}>Încarcă lista de autori</button>
                        </div>

                        <div className="card-form">
                            <input
                                type="text"
                                placeholder="Nume autor..."
                                value={newAuthorName}
                                onChange={(e) => setNewAuthorName(e.target.value)}
                            />
                            <button onClick={createAuthor} className="btn-secondary">
                                Adaugă autor
                            </button>
                        </div>

                        {authors.length > 0 ? (
                            <ul className="list">
                                {authors.map((a) => (
                                    <li key={a.id} className="list-item">
                                        <div className="list-item-main">
                                            <span className="list-item-title">{a.name}</span>
                                            <span className="list-item-badge">ID {a.id}</span>
                                        </div>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p className="empty-state">Nu există autori încă.</p>
                        )}
                    </section>

                    <section className="card">
                        <div className="card-header">
                            <div className="card-icon books">B</div>
                            <div>
                                <h2 className="card-title">Cărți (Interne)</h2>
                                <p className="card-subtitle">
                                    Gestionarea cărților din baza de date locală.
                                </p>
                            </div>
                        </div>

                        <div className="card-actions">
                            <button onClick={loadBooks}>Încarcă lista de cărți</button>
                        </div>

                        <div className="card-form">
                            <input
                                type="text"
                                placeholder="Titlu carte..."
                                value={newBookTitle}
                                onChange={(e) => setNewBookTitle(e.target.value)}
                            />
                            <input
                                type="number"
                                placeholder="ID Autor..."
                                value={newBookAuthorId}
                                onChange={(e) => setNewBookAuthorId(e.target.value)}
                            />
                            <button onClick={createBook} className="btn-secondary">
                                Adaugă carte
                            </button>
                        </div>

                        {books.length > 0 ? (
                            <ul className="list">
                                {books.map((b) => (
                                    <li key={b.id} className="list-item">
                                        <div className="list-item-main">
                                            <span className="list-item-title">{b.title}</span>
                                            <span className="list-item-sub">Autor ID: {b.authorId}</span>
                                        </div>
                                        <span className="list-item-badge">ID {b.id}</span>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p className="empty-state">Nu există cărți interne încă.</p>
                        )}
                    </section>

                    {/* OPENLIBRARY */}
                    <section className="card card-wide">
                        <div className="card-header">
                            <div className="card-icon books">📚</div>
                            <div>
                                <h2 className="card-title">Căutare OpenLibrary</h2>
                                <p className="card-subtitle">
                                    Caută rapid titluri prin serviciul extern de cărți.
                                </p>
                            </div>
                        </div>

                        <div className="card-form">
                            <input
                                type="text"
                                placeholder="Titlu carte (ex: Harry Potter)"
                                value={title}
                                onChange={(e) => setTitle(e.target.value)}
                            />
                            <button onClick={searchOpenLibrary} className="btn-secondary">
                                Caută
                            </button>
                        </div>

                        {externalBooks.length > 0 ? (
                            <ul className="list">
                                {externalBooks.map((b, idx) => (
                                    <li key={idx} className="list-item">
                                        <div className="list-item-main">
                                            <span className="list-item-title">{b.title}</span>
                                            <span className="list-item-sub">
                                                {b.author || "Autor necunoscut"}
                                            </span>
                                        </div>
                                        <div className="list-item-meta">
                                            {b.firstPublishYear && (
                                                <span className="pill">
                                                    An: {b.firstPublishYear}
                                                </span>
                                            )}
                                            {b.isbn && <span className="pill">ISBN {b.isbn}</span>}
                                        </div>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p className="empty-state">
                                Introdu un titlu și pornește o căutare.
                            </p>
                        )}
                    </section>
                </div>
            </main>
        </div>
    );
}

export default App;