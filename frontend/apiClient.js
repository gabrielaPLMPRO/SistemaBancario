export async function request(path, { method = 'GET', body } = {}) {
  let response;
  try {
    response = await fetch(`/api${path}`, {
      method,
      headers: body ? { 'Content-Type': 'application/json' } : {},
      ...(body ? { body: JSON.stringify(body) } : {})
    });
  } catch {
    throw new Error('Não foi possível conectar. Confira a conexão e tente novamente.');
  }
  const data = await response.json().catch(() => ({}));
  if (!response.ok) throw new Error(data.mensagem || 'Não foi possível concluir a operação. Tente novamente.');
  return data;
}
