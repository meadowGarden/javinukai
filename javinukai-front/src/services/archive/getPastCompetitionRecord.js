export default async function (page, limit, sortBy, sortDesc, name) {
  console.log("page -> " + page);
  console.log("limit -> " + limit);
  console.log("sortBy -> " + sortBy);
  console.log("sortDesc -> " + sortDesc);
  console.log("name -> " + name);

  const res = await fetch(
    `${
      import.meta.env.VITE_BACKEND
    }/api/v1/archivedContests?page=${page}&limit=${limit}&sortBy=${sortBy}&sortDesc=${sortDesc}${
      name ? "&name=" + name : ""
    }`,
    {
      method: "GET",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "User-Agent": "react-front",
      },
    }
  );
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.title);
  }

  return await res.json();
}