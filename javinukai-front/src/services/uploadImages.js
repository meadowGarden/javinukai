export default async function ({ data, t }) {
  console.log(data.get("image"));
  const res = await fetch(`${import.meta.env.VITE_BACKEND}/api/v1/images`, {
    method: "POST",
    mode: "cors",
    cache: "no-cache",
    credentials: "include",
    body: data,
  });
  const dataRes = await res.json();
  if (!res.ok) {
    console.log(dataRes);
    switch (dataRes.title) {
      case "PHOTO_VALIDATION_ERROR":
        throw new Error(
          t("uploadImages.photoValidationError", { photoName: dataRes.detail })
        );
      case "PHOTO_PROCESSING_ERROR":
        throw new Error(t("uploadImages.photoProcessingError"));
      default:
        throw new Error(t("uploadImages.photoGeneralError"));
    }
  }
  return await dataRes;
}
