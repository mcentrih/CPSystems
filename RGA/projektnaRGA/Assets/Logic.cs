using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class Logic : MonoBehaviour
{
    public GameObject tablicaGO;
    public List<GameObject> abeceda;

    public List<GameObject> charLoc;

    string tablica = "";

    void Start()
    {
        GetTablicaData();
    }

    private void Update()
    {
        if (Input.touchCount > 0 && Input.touches[0].phase == TouchPhase.Began)
        {
            Ray ray = Camera.current.ScreenPointToRay(Input.GetTouch(0).position);
            RaycastHit hit;
            if (Physics.Raycast(ray, out hit))
            {
                if (hit.collider.name == tablicaGO.transform.name)
                {
                    GetTablicaData();
                    tablica = tablica.ToLower();
                    for (int i = 0; i < tablica.Length; i++)
                    {
                        int pos = (int)tablica[i] - 97;
                        if (pos < 0)
                        {
                            pos += 48;
                            pos += 27;
                        }
                        if (pos == 21 || pos == 22 || pos == 23) continue;
                        Debug.Log($"POS: {pos}; char: {tablica[i]}");
                        Quaternion quaternion = transform.rotation;
                        quaternion.y -= 180;
                        Instantiate(abeceda[pos], charLoc[i].transform.position, quaternion);
                    }
                }
            }
        }

    }

    protected string tablicaDataURL = "http://192.168.1.26/CPSystems/RAIN/getDataTablica.php";

    public void GetTablicaData()
    {
        _ = StartCoroutine(GetTablica());
    }

    IEnumerator GetTablica()
    {
        UnityWebRequest www = UnityWebRequest.Get(tablicaDataURL);
        yield return www.SendWebRequest();
        tablica = www.downloadHandler.text;

        Debug.Log(tablica);
    }
}